package model;

public class GetterSetter {

    private int serialNumber;
    private String carName;
    private String modelNumber;
    private String brand;
    private double price;
    private int stock;
    private String fuelType;
    private String transmission;

    public GetterSetter(int serialNumber, String carName, String modelNumber,
                        String brand, double price, int stock,
                        String fuelType, String transmission) {
        this.serialNumber = serialNumber;
        this.carName = carName;
        this.modelNumber = modelNumber;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.fuelType = fuelType;
        this.transmission = transmission;
    }

    // Getters
    public int getSerialNumber() { return serialNumber; }
    public String getCarName() { return carName; }
    public String getModelNumber() { return modelNumber; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getFuelType() { return fuelType; }
    public String getTransmission() { return transmission; }

    // Setters
    public void setCarName(String carName) { this.carName = carName; }
    public void setModelNumber(String modelNumber) { this.modelNumber = modelNumber; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public boolean isAvailable() { return stock > 0; }
    public boolean buyCar() { if(stock>0){stock--;return true;} return false; }

    @Override
    public String toString() {
        return serialNumber + "," + carName + "," + modelNumber + "," + brand + "," +
                price + "," + stock + "," + fuelType + "," + transmission;
    }
}
