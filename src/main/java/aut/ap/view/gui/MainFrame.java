package aut.ap.view.gui;

import aut.ap.model.Email;
import aut.ap.model.User;
import aut.ap.service.EmailService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {
    private final User user;
    private final EmailService emailService;
    private final JTable emailTable;
    private final DefaultTableModel tableModel;
    private final JPanel formPanel;
    private final JLabel statusLabel;

    public MainFrame(User user) {
        super("Milou Email Service - Main");
        System.out.println("Initializing MainFrame for user: " + user.getEmail());
        this.user = user;
        this.emailService = new EmailService();
        setSize(900, 600);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(Color.RED);
        System.out.println("statusLabel initialized");
        add(statusLabel, BorderLayout.SOUTH);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getName(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        ImageIcon milouIcon = new ImageIcon(getClass().getResource("/images/milou.jpg"));
        JLabel imageLabel = new JLabel();
        if (milouIcon.getImage() != null) {
            imageLabel.setIcon(milouIcon);
            System.out.println("Milou image loaded successfully");
        } else {
            imageLabel.setText("Milou");
            imageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            System.out.println("Milou image not found, using placeholder");
        }
        headerPanel.add(imageLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 5, 5));
        buttonPanel.setBackground(new Color(240, 240, 240));
        JButton sendButton = new JButton("Send Email");
        JButton allEmailsButton = new JButton("All Emails");
        JButton sentEmailsButton = new JButton("Sent Emails");
        JButton unreadEmailsButton = new JButton("Unread Emails");
        JButton viewByCodeButton = new JButton("View by Code");
        JButton replyButton = new JButton("Reply");
        JButton forwardButton = new JButton("Forward");
        JButton logoutButton = new JButton("Log Out");
        styleButton(sendButton, new Color(0, 153, 76));
        styleButton(allEmailsButton, new Color(0, 153, 76));
        styleButton(sentEmailsButton, new Color(0, 153, 76));
        styleButton(unreadEmailsButton, new Color(0, 153, 76));
        styleButton(viewByCodeButton, new Color(0, 153, 76));
        styleButton(replyButton, new Color(0, 153, 76));
        styleButton(forwardButton, new Color(0, 153, 76));
        styleButton(logoutButton, new Color(0, 102, 204));

        buttonPanel.add(sendButton);
        buttonPanel.add(allEmailsButton);
        buttonPanel.add(sentEmailsButton);
        buttonPanel.add(unreadEmailsButton);
        buttonPanel.add(viewByCodeButton);
        buttonPanel.add(replyButton);
        buttonPanel.add(forwardButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.WEST);

        String[] columns = {"Code", "Subject", "Sender", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        emailTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(emailTable);
        add(tableScroll, BorderLayout.CENTER);

        formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(new Color(240, 240, 240));
        JLabel formPlaceholder = new JLabel("Select an action", JLabel.CENTER);
        formPanel.add(formPlaceholder, BorderLayout.CENTER);
        add(formPanel, BorderLayout.EAST);

        updateEmailTable(emailService.getAllEmails(user));
        System.out.println("Initial email table loaded");

        sendButton.addActionListener(e -> showSendForm());
        allEmailsButton.addActionListener(e -> {
            try {
                updateEmailTable(emailService.getAllEmails(user));
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });
        sentEmailsButton.addActionListener(e -> {
            try {
                updateEmailTable(emailService.getSentEmails(user));
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });
        unreadEmailsButton.addActionListener(e -> {
            try {
                updateEmailTable(emailService.getUnreadEmails(user));
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });
        viewByCodeButton.addActionListener(e -> showViewByCodeForm());
        replyButton.addActionListener(e -> showReplyForm());
        forwardButton.addActionListener(e -> showForwardForm());
        logoutButton.addActionListener(e -> {
            System.out.println("Logging out user: " + user.getEmail());
            dispose();
            new LoginFrame().setVisible(true);
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void updateEmailTable(List<Email> emails) {
        tableModel.setRowCount(0);
        for (Email email : emails) {
            tableModel.addRow(new Object[]{
                    email.getCode(),
                    email.getSubject().length() > 30 ? email.getSubject().substring(0, 27) + "..." : email.getSubject(),
                    email.getSender().getEmail(),
                    email.getSentDate().toLocalDate()
            });
        }
        try {
            statusLabel.setText(emails.isEmpty() ? "No emails found." : emails.size() + " emails loaded.");
        } catch (Exception ex) {
            System.err.println("Failed to update status label: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showSendForm() {
        formPanel.removeAll();
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.setBackground(new Color(240, 240, 240));

        JLabel toLabel = new JLabel("To (comma-separated):");
        JTextField toField = new JTextField(20);
        JLabel subjectLabel = new JLabel("Subject:");
        JTextField subjectField = new JTextField(20);
        JLabel bodyLabel = new JLabel("Body:");
        JTextArea bodyArea = new JTextArea(5, 20);
        JScrollPane bodyScroll = new JScrollPane(bodyArea);
        JButton submitButton = new JButton("Send");
        styleButton(submitButton, new Color(0, 153, 76));

        form.add(toLabel);
        form.add(toField);
        form.add(subjectLabel);
        form.add(subjectField);
        form.add(bodyLabel);
        form.add(bodyScroll);
        form.add(new JLabel(""));
        form.add(submitButton);

        submitButton.addActionListener(e -> {
            String recipients = toField.getText().trim();
            String subject = subjectField.getText().trim();
            String body = bodyArea.getText().trim();
            try {
                String code = emailService.sendEmail(user, recipients, subject, body);
                statusLabel.setText("Email sent! Code: " + code);
                toField.setText("");
                subjectField.setText("");
                bodyArea.setText("");
                updateEmailTable(emailService.getAllEmails(user));
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });

        formPanel.add(form, BorderLayout.CENTER);
        formPanel.revalidate();
        formPanel.repaint();
    }

    private void showViewByCodeForm() {
        formPanel.removeAll();
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setBackground(new Color(240, 240, 240));

        JLabel codeLabel = new JLabel("Email Code:");
        JTextField codeField = new JTextField(20);
        JButton submitButton = new JButton("View");
        styleButton(submitButton, new Color(0, 153, 76));

        form.add(codeLabel);
        form.add(codeField);
        form.add(new JLabel(""));
        form.add(submitButton);

        JTextArea detailsArea = new JTextArea(10, 20);
        detailsArea.setEditable(false);
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        formPanel.add(detailsScroll, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            String code = codeField.getText().trim();
            try {
                Email email = emailService.getEmailByCode(user, code);
                detailsArea.setText(
                        "Code: " + email.getCode() + "\n" +
                                "Subject: " + email.getSubject() + "\n" +
                                "Sender: " + email.getSender().getEmail() + "\n" +
                                "Date: " + email.getSentDate() + "\n" +
                                "Recipients: " + email.getRecipients().stream()
                                .map(r -> r.getRecipient().getEmail())
                                .collect(Collectors.joining(", ")) + "\n" +
                                "Body: " + email.getBody()
                );
                statusLabel.setText("Email loaded.");
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
                detailsArea.setText("");
            }
        });

        formPanel.add(form, BorderLayout.NORTH);
        formPanel.revalidate();
        formPanel.repaint();
    }

    private void showReplyForm() {
        formPanel.removeAll();
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBackground(new Color(240, 240, 240));

        JLabel codeLabel = new JLabel("Email Code:");
        JTextField codeField = new JTextField(20);
        JLabel bodyLabel = new JLabel("Reply Body:");
        JTextArea bodyArea = new JTextArea(5, 20);
        JScrollPane bodyScroll = new JScrollPane(bodyArea);
        JButton submitButton = new JButton("Reply");
        styleButton(submitButton, new Color(0, 153, 76));

        form.add(codeLabel);
        form.add(codeField);
        form.add(bodyLabel);
        form.add(bodyScroll);
        form.add(new JLabel(""));
        form.add(submitButton);

        submitButton.addActionListener(e -> {
            String code = codeField.getText().trim();
            String body = bodyArea.getText().trim();
            try {
                String newCode = emailService.replyToEmail(user, code, body);
                statusLabel.setText("Reply sent! New code: " + newCode);
                codeField.setText("");
                bodyArea.setText("");
                updateEmailTable(emailService.getAllEmails(user));
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });

        formPanel.add(form, BorderLayout.CENTER);
        formPanel.revalidate();
        formPanel.repaint();
    }

    private void showForwardForm() {
        formPanel.removeAll();
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBackground(new Color(240, 240, 240));

        JLabel codeLabel = new JLabel("Email Code:");
        JTextField codeField = new JTextField(20);
        JLabel toLabel = new JLabel("To (comma-separated):");
        JTextField toField = new JTextField(20);
        JButton submitButton = new JButton("Forward");
        styleButton(submitButton, new Color(0, 153, 76));

        form.add(codeLabel);
        form.add(codeField);
        form.add(toLabel);
        form.add(toField);
        form.add(new JLabel(""));
        form.add(submitButton);

        submitButton.addActionListener(e -> {
            String code = codeField.getText().trim();
            String recipients = toField.getText().trim();
            try {
                String newCode = emailService.forwardEmail(user, code, recipients);
                statusLabel.setText("Email forwarded! New code: " + newCode);
                codeField.setText("");
                toField.setText("");
                updateEmailTable(emailService.getAllEmails(user));
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });

        formPanel.add(form, BorderLayout.CENTER);
        formPanel.revalidate();
        formPanel.repaint();
    }
}
