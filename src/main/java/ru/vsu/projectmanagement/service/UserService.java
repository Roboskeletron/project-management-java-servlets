package ru.vsu.projectmanagement.service;

import ru.vsu.projectmanagement.domain.User;
import ru.vsu.projectmanagement.exception.EntityNotFoundException;
import ru.vsu.projectmanagement.model.UpdateUserRequest;
import ru.vsu.projectmanagement.model.UserDto;
import ru.vsu.projectmanagement.repository.UserRepository;

import java.sql.SQLException;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int login(String email, String password) throws EntityNotFoundException, SQLException {
        var user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        if (!password.equals(user.getPassword())) {
            throw new EntityNotFoundException();
        }

        return user.getId();
    }

    public int registerUser(String username, String email, String password) throws SQLException {
        var user = new User(0, username, email, password);

        return userRepository.create(user).getId();
    }

    public UserDto getUserById(int id) throws SQLException, EntityNotFoundException {
        var user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }

    public void updateUser(UpdateUserRequest updateUserRequest) throws SQLException, EntityNotFoundException {
        var existingUser = userRepository.findById(updateUserRequest.id()).orElseThrow(EntityNotFoundException::new);

        existingUser.setUsername(updateUserRequest.username().orElse(existingUser.getUsername()));
        existingUser.setPassword(updateUserRequest.password().orElse(existingUser.getPassword()));

        userRepository.update(existingUser);
    }
}
