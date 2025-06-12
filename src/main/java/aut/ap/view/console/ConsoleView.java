package aut.ap.view.console;

import aut.ap.model.Email;
import aut.ap.model.User;
import aut.ap.service.EmailService;
import aut.ap.service.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


//basic console view od the app
public class ConsoleView {
    private final UserService userService;
    private final EmailService emailService;
    private final Scanner scanner;
    private User currentUser;

    public ConsoleView() {
        this.userService = new UserService();
        this.emailService = new EmailService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to Milou Email Service!");
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n=== Login Menu ===");
        System.out.println("1. Sign Up");
        System.out.println("2. Log In");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> signUp();
            case "2" -> logIn();
            case "3" -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            default -> System.out.println("Invalid option. Try again.");
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("Logged in as: " + currentUser.getEmail());
        System.out.println("1. Send Email");
        System.out.println("2. View All Emails");
        System.out.println("3. View Sent Emails");
        System.out.println("4. View Unread Emails");
        System.out.println("5. View Email by Code");
        System.out.println("6. Reply to Email");
        System.out.println("7. Forward Email");
        System.out.println("8. Log Out");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> sendEmail();
            case "2" -> viewAllEmails();
            case "3" -> viewSentEmails();
            case "4" -> viewUnreadEmails();
            case "5" -> viewEmailByCode();
            case "6" -> replyToEmail();
            case "7" -> forwardEmail();
            case "8" -> {
                System.out.println("Logged out successfully.");
                currentUser = null;
            }
            default -> System.out.println("Invalid option. Try again.");
        }
    }

    private void signUp() {
        System.out.println("\n=== Sign Up ===");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email (username only, e.g., 'alice' for alice@milou.com): ");
        String email = scanner.nextLine();
        System.out.print("Enter password (min 8 characters): ");
        String password = scanner.nextLine();

        try {
            userService.signup(name, email, password);
            System.out.println("Sign up successful! Please log in.");
        } catch (Exception e) {
            System.out.println("Sign up failed: " + e.getMessage());
        }
    }

    private void logIn() {
        System.out.println("\n=== Log In ===");
        System.out.print("Enter email (username or full email): ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            currentUser = userService.login(email, password);
            System.out.println("Login successful! Welcome, " + currentUser.getName());
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void sendEmail() {
        System.out.println("\n=== Send Email ===");
        System.out.print("Enter recipient emails (comma-separated, e.g., bob, charlie): ");
        String recipients = scanner.nextLine();
        System.out.print("Enter subject: ");
        String subject = scanner.nextLine();
        System.out.print("Enter body: ");
        String body = scanner.nextLine();

        try {
            String code = emailService.sendEmail(currentUser, recipients, subject, body);
            System.out.println("Email sent successfully! Code: " + code);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }

    private void viewAllEmails() {
        System.out.println("\n=== All Emails ===");
        try {
            List<Email> emails = emailService.getAllEmails(currentUser);
            displayEmails(emails);
        } catch (Exception e) {
            System.out.println("Failed to retrieve emails: " + e.getMessage());
        }
    }

    private void viewSentEmails() {
        System.out.println("\n=== Sent Emails ===");
        try {
            List<Email> emails = emailService.getSentEmails(currentUser);
            displayEmails(emails);
        } catch (Exception e) {
            System.out.println("Failed to retrieve sent emails: " + e.getMessage());
        }
    }

    private void viewUnreadEmails() {
        System.out.println("\n=== Unread Emails ===");
        try {
            List<Email> emails = emailService.getUnreadEmails(currentUser);
            displayEmails(emails);
        } catch (Exception e) {
            System.out.println("Failed to retrieve unread emails: " + e.getMessage());
        }
    }

    private void viewEmailByCode() {
        System.out.println("\n=== View Email by Code ===");
        System.out.print("Enter email code: ");
        String code = scanner.nextLine();

        try {
            Email email = emailService.getEmailByCode(currentUser, code);
            displayEmailDetails(email);
        } catch (Exception e) {
            System.out.println("Failed to view email: " + e.getMessage());
        }
    }

    private void replyToEmail() {
        System.out.println("\n=== Reply to Email ===");
        System.out.print("Enter email code: ");
        String code = scanner.nextLine();
        System.out.print("Enter reply body: ");
        String body = scanner.nextLine();

        try {
            String newCode = emailService.replyToEmail(currentUser, code, body);
            System.out.println("Reply sent successfully! New code: " + newCode);
        } catch (Exception e) {
            System.out.println("Failed to reply: " + e.getMessage());
        }
    }

    private void forwardEmail() {
        System.out.println("\n=== Forward Email ===");
        System.out.print("Enter email code: ");
        String code = scanner.nextLine();
        System.out.print("Enter recipient emails (comma-separated): ");
        String recipients = scanner.nextLine();

        try {
            String newCode = emailService.forwardEmail(currentUser, code, recipients);
            System.out.println("Email forwarded successfully! New code: " + newCode);
        } catch (Exception e) {
            System.out.println("Failed to forward email: " + e.getMessage());
        }
    }

    private void displayEmails(List<Email> emails) {
        if (emails.isEmpty()) {
            System.out.println("No emails found.");
            return;
        }
        System.out.printf("%-10s %-20s %-30s %-15s%n", "Code", "Subject", "Sender", "Sent Date");
        System.out.println("-".repeat(75));
        for (Email email : emails) {
            System.out.printf("%-10s %-20s %-30s %-15s%n",
                    email.getCode(),
                    email.getSubject().length() > 20 ? email.getSubject().substring(0, 17) + "..." : email.getSubject(),
                    email.getSender().getEmail(),
                    email.getSentDate().toLocalDate());
        }
    }

    private void displayEmailDetails(Email email) {
        System.out.println("\n=== Email Details ===");
        System.out.println("Code: " + email.getCode());
        System.out.println("Subject: " + email.getSubject());
        System.out.println("Sender: " + email.getSender().getEmail());
        System.out.println("Sent Date: " + email.getSentDate());
        System.out.println("Recipients: " + email.getRecipients().stream()
                .map(r -> r.getRecipient().getEmail())
                .collect(Collectors.joining(", ")));
        System.out.println("Body: " + email.getBody());
    }
}