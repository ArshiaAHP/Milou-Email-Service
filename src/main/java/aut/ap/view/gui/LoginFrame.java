package aut.ap.view.gui;

import aut.ap.model.User;
import aut.ap.service.UserService;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final UserService userService;
    private final JLabel errorLabel;

    public LoginFrame() {
        super("Milou Email Service - Login");
        this.userService = new UserService();
        setSize(600, 400);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Milou Email Service", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon milouIcon = new ImageIcon(getClass().getResource("/images/milou.jpg"));
        JLabel imageLabel = new JLabel();
        if (milouIcon.getImage() != null) {
            imageLabel.setIcon(milouIcon);
        } else {
            imageLabel.setText("Milou Image Placeholder");
            imageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        }
        imagePanel.add(imageLabel);
        add(imagePanel, BorderLayout.WEST);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(new Color(240, 240, 240));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Log In");
        loginButton.setBackground(new Color(0, 153, 76));
        loginButton.setForeground(Color.WHITE);
        JButton signupButton = new JButton("Sign Up");
        signupButton.setBackground(new Color(0, 102, 204));
        signupButton.setForeground(Color.WHITE);

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(new JLabel(""));
        formPanel.add(loginButton);
        formPanel.add(new JLabel(""));
        formPanel.add(signupButton);

        add(formPanel, BorderLayout.CENTER);

        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        add(errorLabel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            try {
                User user = userService.login(email, password);
                dispose();
                new MainFrame(user).setVisible(true);
            } catch (Exception ex) {
                errorLabel.setText("Error: " + ex.getMessage());
            }
        });

        signupButton.addActionListener(e -> {
            dispose();
            new SignUpFrame().setVisible(true);
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}