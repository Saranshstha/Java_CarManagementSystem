package controller;

import model.GetterSetter;
import java.io.*;
import java.util.*;

public class Car {

    private static ArrayList<GetterSetter> carList = new ArrayList<>();
    private static Stack<GetterSetter> undoStack = new Stack<>();

    // ---------------- Add Car ----------------
    public static boolean addCar(GetterSetter car){
        if(getCarBySerial(car.getSerialNumber())!=null) return false; // prevent duplicate serial
        carList.add(car);
        undoStack.push(car);
        saveToFile();
        return true;
    }

    // ---------------- Undo ----------------
    public static void undoLastCar(){
        if(!undoStack.isEmpty()){
            GetterSetter last = undoStack.pop();
            carList.remove(last);
            saveToFile();
        }
    }

    // ---------------- Restock ----------------
    public static boolean restockCar(int serial,int quantity){
        GetterSetter car = getCarBySerial(serial);
        if(car!=null){ car.setStock(car.getStock()+quantity); saveToFile(); return true; }
        return false;
    }

    // ---------------- Delete ----------------
    public static boolean deleteCar(int serial){
        GetterSetter car = getCarBySerial(serial);
        if(car!=null){ carList.remove(car); saveToFile(); return true; }
        return false;
    }

    // ---------------- Update ----------------
    public static boolean updateCar(int serial, double price, int stock){
        GetterSetter car = getCarBySerial(serial);
        if(car!=null){ car.setPrice(price); car.setStock(stock); saveToFile(); return true; }
        return false;
    }

    // ---------------- Buy ----------------
    public static boolean buyCar(int serial){
        GetterSetter car = getCarBySerial(serial);
        if(car!=null && car.getStock()>0){ car.setStock(car.getStock()-1); saveToFile(); return true; }
        return false;
    }

    // ---------------- Search ----------------
    public static ArrayList<GetterSetter> linearSearch(String keyword){
        ArrayList<GetterSetter> result = new ArrayList<>();
        for(GetterSetter car: carList){
            if(car.getCarName().toLowerCase().contains(keyword.toLowerCase()) ||
               car.getModelNumber().toLowerCase().contains(keyword.toLowerCase())){
                result.add(car);
            }
        }
        return result;
    }

    public static GetterSetter binarySearchBySerial(int serial){
        insertionSortBySerial();
        int low=0,high=carList.size()-1;
        while(low<=high){
            int mid=(low+high)/2;
            if(carList.get(mid).getSerialNumber()==serial) return carList.get(mid);
            if(carList.get(mid).getSerialNumber()<serial) low=mid+1;
            else high=mid-1;
        }
        return null;
    }

    // ---------------- Sort ----------------
    public static void bubbleSortByName(){
        for(int i=0;i<carList.size()-1;i++){
            for(int j=0;j<carList.size()-i-1;j++){
                if(carList.get(j).getCarName().compareToIgnoreCase(carList.get(j+1).getCarName())>0){
                    Collections.swap(carList,j,j+1);
                }
            }
        }
    }

    public static void insertionSortBySerial(){
        for(int i=1;i<carList.size();i++){
            GetterSetter key = carList.get(i);
            int j=i-1;
            while(j>=0 && carList.get(j).getSerialNumber()>key.getSerialNumber()){
                carList.set(j+1,carList.get(j));
                j--;
            }
            carList.set(j+1,key);
        }
    }

    // ---------------- Dashboard ----------------
    public static int totalCars(){ return carList.size(); }
    public static int totalStock(){ int total=0; for(GetterSetter car: carList) total+=car.getStock(); return total; }
    public static double totalInventoryValue(){ double total=0; for(GetterSetter car: carList) total+=car.getPrice()*car.getStock(); return total; }

    // ---------------- File Save / Load ----------------
    public static void saveToFile(){
        try(PrintWriter writer = new PrintWriter(new FileWriter("cars.txt"))){
            for(GetterSetter car: carList){
                writer.println(car.toString());
            }
        } catch(Exception e){ System.out.println("Error Saving File"); }
    }

    public static void loadFromFile(){
        carList.clear();
        try{
            File file=new File("cars.txt");
            if(!file.exists()) return;
            Scanner sc=new Scanner(file);
            while(sc.hasNextLine()){
                String[] d = sc.nextLine().split(",");
                if(d.length<8) continue;
                GetterSetter car=new GetterSetter(
                        Integer.parseInt(d[0]),d[1],d[2],d[3],
                        Double.parseDouble(d[4]),Integer.parseInt(d[5]),d[6],d[7]);
                carList.add(car);
            }
            sc.close();
        }catch(Exception e){ System.out.println("Error Loading File"); }
    }

    // ---------------- Getters ----------------
    public static ArrayList<GetterSetter> getCarList(){ return carList; }
    private static GetterSetter getCarBySerial(int serial){
        for(GetterSetter car: carList) if(car.getSerialNumber()==serial) return car;
        return null;
    }
}
