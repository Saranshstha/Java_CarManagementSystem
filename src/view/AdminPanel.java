package view;

import controller.Car;
import model.GetterSetter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AdminPanel extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;

    public AdminPanel() {
        setTitle("Admin Panel - Car Management");
        setSize(1200,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240,242,245));
        setLayout(null);

        // ---------------- Title ----------------
        JLabel title = new JLabel("🚗 Admin Panel");
        title.setBounds(30, 10, 300, 40);
        title.setFont(new Font("Helvetica Neue", Font.BOLD, 28));
        title.setForeground(new Color(20,20,20));
        add(title);

        // ---------------- Logout ----------------
        JButton logout = createButton("Logout",1050,15,120,35,new Color(255,69,58));
        logout.addActionListener(e -> { dispose(); new LoginGUI(); });
        add(logout);

        // ---------------- Live Search ----------------
        searchField = new JTextField();
        searchField.setBounds(30,70,200,30);
        add(searchField);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
        });

        // ---------------- Buttons ----------------
        JButton addBtn = createButton("Add Car",250,70,120,35,new Color(52,199,89));
        JButton updateBtn = createButton("Update Car",390,70,140,35,new Color(0,122,255));
        JButton deleteBtn = createButton("Delete Car",550,70,140,35,new Color(255,149,0));
        JButton restockBtn = createButton("Restock Car",710,70,150,35,new Color(88,86,214));
        JButton undoBtn = createButton("Undo Add",880,70,120,35,new Color(255,45,85));
        JButton binaryBtn = createButton("Binary Search",1020,70,140,35,new Color(255,204,0));
        add(addBtn); add(updateBtn); add(deleteBtn); add(restockBtn);
        add(undoBtn); add(binaryBtn);

        // ---------------- Table ----------------
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setRowHeight(28);
        table.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        model.setColumnIdentifiers(new String[]{
                "Serial","Name","Model","Brand","Price","Stock","Fuel","Transmission"
        });
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(30,120,1080,450);
        add(pane);

        Car.loadFromFile();
        loadTable(Car.getCarList());

        // ---------------- Button Actions ----------------
        addBtn.addActionListener(e -> openAddDialog());
        updateBtn.addActionListener(e -> openUpdateDialog());
        deleteBtn.addActionListener(e -> openDeleteDialog());
        restockBtn.addActionListener(e -> openRestockDialog());
        undoBtn.addActionListener(e -> { Car.undoLastCar(); loadTable(Car.getCarList()); });
        binaryBtn.addActionListener(e -> openBinarySearchDialog());

        setVisible(true);
    }

    // ---------------- Button Creator ----------------
    private JButton createButton(String text,int x,int y,int w,int h,Color bg){
        JButton btn = new JButton(text);
        btn.setBounds(x,y,w,h);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Helvetica Neue",Font.BOLD,14));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){ btn.setBackground(bg.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt){ btn.setBackground(bg); }
        });
        return btn;
    }

    // ---------------- Live Search ----------------
    private void updateSearch(){
        String keyword = searchField.getText().trim();
        loadTable(Car.linearSearch(keyword));
    }

    // ---------------- Load Table ----------------
    private void loadTable(ArrayList<GetterSetter> list){
        model.setRowCount(0);
        for(GetterSetter c : list){
            model.addRow(new Object[]{
                    c.getSerialNumber(), c.getCarName(), c.getModelNumber(),
                    c.getBrand(), c.getPrice(), c.getStock(),
                    c.getFuelType(), c.getTransmission()
            });
        }
    }

    // ---------------- Add Car ----------------
    private void openAddDialog(){
        JDialog dialog = createCarDialog("Add Car", null);
        dialog.setVisible(true);
    }

    // ---------------- Update Car ----------------
    private void openUpdateDialog(){
        int row = table.getSelectedRow();
        if(row==-1){ JOptionPane.showMessageDialog(this,"Select a car to update!"); return; }
        int serial = (int) table.getValueAt(row,0);
        GetterSetter car = null;
        for(GetterSetter c: Car.getCarList()){ if(c.getSerialNumber()==serial){ car=c; break; } }
        if(car==null) return;
        JDialog dialog = createCarDialog("Update Car", car);
        dialog.setVisible(true);
    }

    // ---------------- Delete Car ----------------
    private void openDeleteDialog(){
        int row = table.getSelectedRow();
        if(row==-1){ JOptionPane.showMessageDialog(this,"Select a car to delete!"); return; }
        int serial = (int) table.getValueAt(row,0);
        int confirm = JOptionPane.showConfirmDialog(this,"Delete car with Serial "+serial+"?","Confirm Delete",JOptionPane.YES_NO_OPTION);
        if(confirm==JOptionPane.YES_OPTION){
            Car.deleteCar(serial);
            loadTable(Car.getCarList());
        }
    }

    // ---------------- Restock Car ----------------
    private void openRestockDialog(){
        int row = table.getSelectedRow();
        if(row==-1){ JOptionPane.showMessageDialog(this,"Select a car to restock!"); return; }
        int serial = (int) table.getValueAt(row,0);
        String qtyStr = JOptionPane.showInputDialog(this,"Enter quantity to restock:");
        try{
            int qty = Integer.parseInt(qtyStr.trim());
            if(qty>0){ Car.restockCar(serial,qty); loadTable(Car.getCarList()); }
            else JOptionPane.showMessageDialog(this,"Quantity must be > 0");
        }catch(Exception e){ JOptionPane.showMessageDialog(this,"Invalid input"); }
    }

    // ---------------- Binary Search ----------------
    private void openBinarySearchDialog(){
        String s = JOptionPane.showInputDialog(this,"Enter Serial Number to search:");
        try{
            int serial = Integer.parseInt(s.trim());
            GetterSetter car = Car.binarySearchBySerial(serial);
            if(car!=null){
                JOptionPane.showMessageDialog(this,
                        "Name: "+car.getCarName()+
                        "\nModel: "+car.getModelNumber()+
                        "\nBrand: "+car.getBrand()+
                        "\nPrice: "+car.getPrice()+
                        "\nStock: "+car.getStock()+
                        "\nFuel: "+car.getFuelType()+
                        "\nTransmission: "+car.getTransmission());
            } else JOptionPane.showMessageDialog(this,"Car not found!");
        }catch(Exception e){ JOptionPane.showMessageDialog(this,"Invalid input"); }
    }

    // ---------------- Single Pop-up Dialog for Add/Update ----------------
    private JDialog createCarDialog(String title, GetterSetter car){
        JDialog dialog = new JDialog(this,title,true);
        dialog.setSize(400,450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel serialLabel = new JLabel("Serial Number:"); JTextField serialField = new JTextField();
        JLabel nameLabel = new JLabel("Car Name:"); JTextField nameField = new JTextField();
        JLabel modelLabel = new JLabel("Model Number:"); JTextField modelField = new JTextField();
        JLabel brandLabel = new JLabel("Brand:"); JTextField brandField = new JTextField();
        JLabel priceLabel = new JLabel("Price:"); JTextField priceField = new JTextField();
        JLabel stockLabel = new JLabel("Stock:"); JTextField stockField = new JTextField();
        JLabel fuelLabel = new JLabel("Fuel Type:"); JTextField fuelField = new JTextField();
        JLabel transLabel = new JLabel("Transmission:"); JTextField transField = new JTextField();

        if(car!=null){
            serialField.setText(""+car.getSerialNumber()); serialField.setEditable(false);
            nameField.setText(car.getCarName());
            modelField.setText(car.getModelNumber());
            brandField.setText(car.getBrand());
            priceField.setText(""+car.getPrice());
            stockField.setText(""+car.getStock());
            fuelField.setText(car.getFuelType());
            transField.setText(car.getTransmission());
        }

        JButton submit = new JButton(car==null?"Add Car":"Update Car");
        submit.setBackground(new Color(52,199,89)); submit.setForeground(Color.WHITE);
        JButton cancel = new JButton("Cancel"); cancel.setBackground(new Color(255,69,58)); cancel.setForeground(Color.WHITE);

        int y=0;
        c.gridx=0;c.gridy=y;c.weightx=0.4;dialog.add(serialLabel,c);
        c.gridx=1;c.weightx=0.6;dialog.add(serialField,c); y++;
        c.gridx=0;c.gridy=y;c.weightx=0.4;dialog.add(nameLabel,c);
        c.gridx=1;c.weightx=0.6;dialog.add(nameField,c); y++;
        c.gridx=0;c.gridy=y;c.weightx=0.4;dialog.add(modelLabel,c);
        c.gridx=1;c.weightx=0.6;dialog.add(modelField,c); y++;
        c.gridx=0;c.gridy=y;c.weightx=0.4;dialog.add(brandLabel,c);
        c.gridx=1;c.weightx=0.6;dialog.add(brandField,c); y++;
        c.gridx=0;c.gridy=y;c.weightx=0.4;dialog.add(priceLabel,c);
        c.gridx=1;c.weightx=0.6;dialog.add(priceField,c); y++;
        c.gridx=0;c.gridy=y;c.weightx=0.4;dialog.add(stockLabel,c);
        c.gridx=1;c.weightx=0.6;dialog.add(stockField,c); y++;
        c.gridx=0;c.gridy=y;c.weightx=0.4;dialog.add(fuelLabel,c);
        c.gridx=1;c.weightx=0.6;dialog.add(fuelField,c); y++;
        c.gridx=0;c.gridy=y;c.weightx=0.4;dialog.add(transLabel,c);
        c.gridx=1;c.weightx=0.6;dialog.add(transField,c); y++;
        c.gridx=0;c.gridy=y;c.weightx=0.5;dialog.add(submit,c);
        c.gridx=1;c.weightx=0.5;dialog.add(cancel,c);

        submit.addActionListener(e -> {
            try{
                int serial = Integer.parseInt(serialField.getText().trim());
                String name=nameField.getText().trim();
                String model=modelField.getText().trim();
                String brand=brandField.getText().trim();
                double price=Double.parseDouble(priceField.getText().trim());
                int stock=Integer.parseInt(stockField.getText().trim());
                String fuel=fuelField.getText().trim();
                String trans=transField.getText().trim();

                if(name.isEmpty()||model.isEmpty()||brand.isEmpty()||fuel.isEmpty()||trans.isEmpty()){
                    JOptionPane.showMessageDialog(dialog,"All fields must be filled!");
                    return;
                }

                if(car==null){ // Add
                    if(!Car.addCar(new GetterSetter(serial,name,model,brand,price,stock,fuel,trans))){
                        JOptionPane.showMessageDialog(dialog,"Serial Number exists!");
                        return;
                    }
                    JOptionPane.showMessageDialog(dialog,"Car added!");
                } else { // Update
                    car.setCarName(name); car.setModelNumber(model);
                    car.setBrand(brand); car.setPrice(price);
                    car.setStock(stock); car.setFuelType(fuel); car.setTransmission(trans);
                    Car.saveToFile();
                    JOptionPane.showMessageDialog(dialog,"Car updated!");
                }

                loadTable(Car.getCarList());
                dialog.dispose();
            }catch(Exception ex){ JOptionPane.showMessageDialog(dialog,"Invalid input!"); }
        });

        cancel.addActionListener(e -> dialog.dispose());

        return dialog;
    }
}
