package view;

import controller.Car;
import model.GetterSetter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CustomerPanel extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;

    public CustomerPanel(){
        setTitle("Customer Panel - Browse Cars");
        setSize(1200,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240,242,245));
        setLayout(null);

        JLabel title = new JLabel("🚗 Browse Cars");
        title.setBounds(30,10,300,40);
        title.setFont(new Font("Helvetica Neue",Font.BOLD,28));
        title.setForeground(new Color(20,20,20));
        add(title);

        JButton logout = new JButton("Logout");
        logout.setBounds(1050,15,120,35);
        logout.setBackground(new Color(255,69,58));
        logout.setForeground(Color.WHITE);
        logout.setFont(new Font("Helvetica Neue",Font.BOLD,14));
        logout.setFocusPainted(false);
        logout.setBorder(BorderFactory.createEmptyBorder());
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logout.addActionListener(e -> { dispose(); new LoginGUI(); });
        add(logout);

        searchField = new JTextField();
        searchField.setBounds(30,70,200,30);
        add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(240,70,100,30);
        searchBtn.setBackground(new Color(0,122,255));
        searchBtn.setForeground(Color.WHITE);
        add(searchBtn);

        JButton sortName = new JButton("Sort A-Z");
        sortName.setBounds(360,70,100,30);
        sortName.setBackground(new Color(52,199,89));
        sortName.setForeground(Color.WHITE);
        add(sortName);

        JButton sortSerial = new JButton("Sort by Serial");
        sortSerial.setBounds(480,70,140,30);
        sortSerial.setBackground(new Color(88,86,214));
        sortSerial.setForeground(Color.WHITE);
        add(sortSerial);

        JButton buyBtn = new JButton("Buy Selected Car");
        buyBtn.setBounds(640,70,160,30);
        buyBtn.setBackground(new Color(255,149,0));
        buyBtn.setForeground(Color.WHITE);
        add(buyBtn);

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setRowHeight(28);
        table.setFont(new Font("Helvetica Neue",Font.PLAIN,14));
        model.setColumnIdentifiers(new String[]{
                "Serial","Name","Model","Brand","Price","Stock","Fuel","Transmission"
        });
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(30,120,1080,450);
        add(pane);

        Car.loadFromFile();
        loadTable(Car.getCarList());

        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            loadTable(Car.linearSearch(keyword));
        });

        sortName.addActionListener(e -> { Car.bubbleSortByName(); loadTable(Car.getCarList()); });
        sortSerial.addActionListener(e -> { Car.insertionSortBySerial(); loadTable(Car.getCarList()); });
        buyBtn.addActionListener(e -> buySelectedCar());

        setVisible(true);
    }

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

    private void buySelectedCar(){
        int row = table.getSelectedRow();
        if(row==-1){ JOptionPane.showMessageDialog(this,"Select a car!"); return; }
        int serial = (int) table.getValueAt(row,0);
        GetterSetter car = Car.binarySearchBySerial(serial);
        if(car.getStock()<=0){ JOptionPane.showMessageDialog(this,"Car out of stock!"); return; }
        Car.buyCar(serial);
        JOptionPane.showMessageDialog(this,"Purchased "+car.getCarName());
        loadTable(Car.getCarList());
    }
}
