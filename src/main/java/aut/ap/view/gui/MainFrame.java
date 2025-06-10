package aut.ap.view.gui;

import aut.ap.model.User;

import java.awt.*;
import java.awt.event.*;

public class MainFrame extends Frame {
    private final User user;

    public MainFrame(User user) {
        super("Milou Email Service - Main");
        this.user = user;
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));

        Panel headerPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(0, 102, 204));
        Label welcomeLabel = new Label("Welcome, " + user.getName(), Label.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);
        add(headerPanel, BorderLayout.NORTH);

        Panel buttonPanel = new Panel(new GridLayout(6, 1, 5, 5));
        buttonPanel.setBackground(new Color(240, 240, 240));
        Button sendButton = new Button("Send Email");
        Button allEmailsButton = new Button("All Emails");
        Button sentEmailsButton = new Button("Sent Emails");
        Button unreadEmailsButton = new Button("Unread Emails");
        Button replyButton = new Button("Reply/Forward");
        Button logoutButton = new Button("Log Out");
        sendButton.setBackground(new Color(0, 153, 76));
        allEmailsButton.setBackground(new Color(0, 153, 76));
        sentEmailsButton.setBackground(new Color(0, 153, 76));
        unreadEmailsButton.setBackground(new Color(0, 153, 76));
        replyButton.setBackground(new Color(0, 153, 76));
        logoutButton.setBackground(new Color(0, 102, 204));
        sendButton.setForeground(Color.WHITE);
        allEmailsButton.setForeground(Color.WHITE);
        sentEmailsButton.setForeground(Color.WHITE);
        unreadEmailsButton.setForeground(Color.WHITE);
        replyButton.setForeground(Color.WHITE);
        logoutButton.setForeground(Color.WHITE);

        buttonPanel.add(sendButton);
        buttonPanel.add(allEmailsButton);
        buttonPanel.add(sentEmailsButton);
        buttonPanel.add(unreadEmailsButton);
        buttonPanel.add(replyButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.WEST);

        Panel listPanel = new Panel(new BorderLayout());
        Label listLabel = new Label("Email List (Placeholder)", Label.CENTER);
        List emailList = new List(20); // AWT List component
        emailList.add("Sample Email 1");
        emailList.add("Sample Email 2");
        listPanel.add(listLabel, BorderLayout.NORTH);
        listPanel.add(emailList, BorderLayout.CENTER);
        add(listPanel, BorderLayout.CENTER);

        Panel contentPanel = new Panel(new BorderLayout());
        Label contentLabel = new Label("Compose/View Email (Placeholder)", Label.CENTER);
        contentPanel.add(contentLabel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.EAST);

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        sendButton.addActionListener(e -> listLabel.setText("Send Email clicked"));
        allEmailsButton.addActionListener(e -> listLabel.setText("All Emails clicked"));
        sentEmailsButton.addActionListener(e -> listLabel.setText("Sent Emails clicked"));
        unreadEmailsButton.addActionListener(e -> listLabel.setText("Unread Emails clicked"));
        replyButton.addActionListener(e -> listLabel.setText("Reply/Forward clicked"));

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setLocationRelativeTo(null);
    }
}