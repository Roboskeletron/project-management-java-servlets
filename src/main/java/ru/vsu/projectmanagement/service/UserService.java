package ru.vsu.projectmanagement.service;

import ru.vsu.projectmanagement.dao.UserDao;
import ru.vsu.projectmanagement.domain.User;
import ru.vsu.projectmanagement.domain.UserRole;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    // WARNING: THIS IS NOT SECURE. Implement proper password hashing (e.g., bcrypt).
    private String hashPassword(String rawPassword) {
        // For demonstration purposes, plain text. Replace with strong hashing.
        return rawPassword; // Placeholder
    }

    // WARNING: THIS IS NOT SECURE if hashPassword is not a real hash.
    private boolean checkPassword(String rawPassword, String hashedPassword) {
        // If using plain text:
        return rawPassword.equals(hashedPassword);
        // If using bcrypt: return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    public Optional<User> registerUser(String username, String rawPassword, String email, String fullName, UserRole role) throws SQLException {
        if (userDao.findByUsername(username).isPresent()) {
            return Optional.empty(); // Username already exists
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hashPassword(rawPassword)); // Store hashed password
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(role != null ? role : UserRole.USER);
        user.setActive(true);
        // createdAt will be set by DB or DAO
        return Optional.of(userDao.create(user));
    }

    public Optional<User> login(String username, String rawPassword) throws SQLException {
        Optional<User> userOpt = userDao.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isActive() && checkPassword(rawPassword, user.getPasswordHash())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findUserById(Long id) throws SQLException {
        return userDao.findById(id);
    }

    public List<User> findAllUsers() throws SQLException {
        return userDao.findAll();
    }

    public void updateUser(User user) throws SQLException {
        // Ensure password is not accidentally changed to plain text if not provided
        // Or handle password change separately
        userDao.update(user);
    }

    public boolean changeUserRole(long userId, UserRole newRole) throws SQLException {
        Optional<User> userOpt = userDao.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(newRole);
            userDao.update(user);
            return true;
        }
        return false;
    }
}
