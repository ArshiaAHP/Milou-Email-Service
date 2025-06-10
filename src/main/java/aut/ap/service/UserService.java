package aut.ap.service;

import aut.ap.dao.UserDao;
import aut.ap.model.User;

public class UserService {
    private final UserDao userDao = new UserDao();

    public User signup(String name, String email, String password) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Name cannot be empty.");
        }
        if (password == null || password.length() < 8) {
            throw new Exception("Password must be at least 8 characters.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Email cannot be empty.");
        }
        String normalizedEmail = email.trim().toLowerCase();
        if (!normalizedEmail.endsWith("@milou.com")) {
            normalizedEmail = normalizedEmail + "@milou.com";
        }
        if (userDao.findByEmail(normalizedEmail) != null) {
            throw new Exception("Email already exists.");
        }
        User user = new User(name.trim(), normalizedEmail, password); // In production, hash the password
        userDao.saveUser(user);
        return user;
    }

    public User login(String email, String password) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Email cannot be empty.");
        }
        if (password == null || password.isEmpty()) {
            throw new Exception("Password cannot be empty.");
        }
        String normalizedEmail = email.trim().toLowerCase();
        if (!normalizedEmail.endsWith("@milou.com")) {
            normalizedEmail = normalizedEmail + "@milou.com";
        }
        User user = userDao.findByEmail(normalizedEmail);
        if (user == null || !user.getPassword().equals(password)) { // In production, compare hashed passwords
            throw new Exception("Invalid email or password.");
        }
        return user;
    }
}