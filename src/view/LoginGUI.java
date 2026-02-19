package view;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {

    public LoginGUI() {
        setTitle("Car Management System - Login");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(240,242,245));

        JLabel title = new JLabel("🚗 Car Management Login");
        title.setFont(new Font("Helvetica Neue", Font.BOLD, 20));
        title.setBounds(50, 20, 300, 30);
        add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 70, 100, 25);
        add(userLabel);
        JTextField userField = new JTextField();
        userField.setBounds(150,70,180,25);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 110, 100, 25);
        add(passLabel);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(150,110,180,25);
        add(passField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150,160,100,30);
        loginBtn.setBackground(new Color(0,122,255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            if(user.equals("admin") && pass.equals("admin")){
                dispose();
                new AdminPanel();
            } else if(user.equals("guest") && pass.equals("guest")){
                dispose();
                new CustomerPanel();
            } else {
                JOptionPane.showMessageDialog(this,"Invalid Credentials!");
            }
        });

        setVisible(true);
    }
}
