# Milou Email Service

The **Milou Email Service** is a desktop email client developed in Java as a final project for the Advanced Programming course. It provides a user-friendly graphical interface built with Swing, allowing users to manage email accounts and perform core email operations. The application integrates with a MySQL database via Hibernate for persistent storage and features fixed-size windows for a consistent user experience.

## Overview

The Milou Email Service enables users to create accounts, log in, and handle email tasks through an intuitive desktop application. Designed with a focus on functionality and clean UI design, it includes a custom logo (Milou) and a structured interface for email management. The project demonstrates proficiency in Java, GUI development, and database integration, fulfilling the requirements of the Final Advanced Programming course.

## Features

- **User Authentication**: Secure signup and login with name, email, and password.
- **Email Management**:
  - Send emails with multiple recipients (comma-separated), subject, and body.
  - View all, sent, or unread emails in a tabular format.
  - Access email details by entering a unique code.
  - Reply to or forward emails using their codes.
- **Logout Functionality**: Return to the login screen to switch users.
- **Branded UI**: Custom Milou image integrated into all windows for visual identity.

## Technology Stack

- **Java**: Core programming language for application logic.
- **Swing & AWT**: Java’s GUI toolkit for building the user interface.
- **Hibernate**:ORM framework for database interactions.
- **MySQL**: Relational database for storing user and email data.
- **Maven**: Build tool for dependency management and project compilation.

## Setup Instructions

To configure the Milou Email Service, update the `hibernate.cfg.xml.template` file with your MySQL credentials:

1. **Locate the Template File**:
   - Find `src/main/resources/hibernate.cfg.xml.template` in the project directory.

2. **Rename and Configure**:
   - Copy `hibernate.cfg.xml.template` to `src/main/resources/hibernate.cfg.xml`.
   - Open `hibernate.cfg.xml` and replace the placeholder values with your MySQL credentials.
   - Replace `your_mysql_password` with your MySQL root password.
   - Ensure the `url` points to the database (create it in MySQL if it does not exist).

3. **Build and Run**:
   - Open the project in an IDE.
   - Refresh `pom.xml`.
   - Run `src/main/java/aut/ap/Main.java` to start the application.

## Screenshots
![image](https://github.com/user-attachments/assets/2f88bae2-94c2-4c47-bcce-67bb3d4619ff)
![image](https://github.com/user-attachments/assets/f5372fe1-c943-4416-9e78-608751cb5ee3)
![image](https://github.com/user-attachments/assets/b3a9b6b2-e985-40b5-8709-59e03010882c)



- **Login Interface**: 600x400 window with email and password fields.
- **Signup Interface**: 600x400 window with name, email, and password fields.
- **Main Interface**: 900x600 window with email table, action buttons, and forms.

## Notes

- **Fixed Window Sizes**: The application uses non-resizable windows to maintain a consistent layout across different screen resolutions.
- **Database**: Ensure MySQL is running and properly configured to avoid connection errors.
- **Course Context**: This project was developed as part of the Final Advanced Programming course, demonstrating skills in Java, GUI design, and database management.

## License

This project is licensed under the AmirKabir University of Technology license.
