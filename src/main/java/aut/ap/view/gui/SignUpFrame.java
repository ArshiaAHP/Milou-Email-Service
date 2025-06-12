package aut.ap.view.gui;

import aut.ap.service.UserService;
import javax.swing.*;
import java.awt.*;

public class SignUpFrame extends JFrame {
    private final UserService userService;
    private final JLabel errorLabel;
    private final JTextField nameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;

    public SignUpFrame() {
        super("Milou Email Service - Sign Up");
        this.userService = new UserService();
        setSize(600, 400);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Sign Up for Milou", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon milouIcon = new ImageIcon(getClass().getResource("/images/milou.jpg"));
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(milouIcon);
        setIconImage(milouIcon.getImage());
        imagePanel.add(imageLabel);
        add(imagePanel, BorderLayout.WEST);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(new Color(240, 240, 240));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        JButton signupButton = new JButton("Sign Up");
        signupButton.setBackground(new Color(0, 153, 76));
        signupButton.setForeground(Color.WHITE);
        JButton loginButton = new JButton("Back to Login");
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(new JLabel(""));
        formPanel.add(signupButton);
        formPanel.add(new JLabel(""));
        formPanel.add(loginButton);

        add(formPanel, BorderLayout.CENTER);

        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        add(errorLabel, BorderLayout.SOUTH);

        signupButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            try {
                userService.signup(name, email, password);
                errorLabel.setText("Sign up successful! Returning to login...");
                nameField.setText("");
                emailField.setText("");
                passwordField.setText("");
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        dispose();
                        new LoginFrame().setVisible(true);
                    }
                }, 1000); // for design purposes :)
            } catch (Exception ex) {
                errorLabel.setText("Error: " + ex.getMessage());
            }
        });

        loginButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}