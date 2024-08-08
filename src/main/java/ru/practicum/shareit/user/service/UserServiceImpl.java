package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private static final String USER_NOT_FOUND = "User not found";

    private boolean checkEmail(String email) {
        return getAllUsers().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserMapper::transformToUserDto)
                .toList();
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.transformToUser(userDto);
        if (checkEmail(user.getEmail())) {
            throw new EmailExistException("Email already exist");
        }
        userRepository.createUser(user);
        return UserMapper.transformToUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        if (!checkUser(userId)) {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
        User user = UserMapper.transformToUser(userDto);
        User updatedUser = userRepository.getUserById(userId);
        if (user.getEmail() != null) {
            if (checkEmail(user.getEmail())) {
                throw new EmailExistException("Email already exist");
            }
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        userRepository.updateUser(updatedUser);
        return UserMapper.transformToUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        if (checkUser(userId)) {
            User user = userRepository.getUserById(userId);
            return UserMapper.transformToUserDto(user);
        }
        throw new ResourceNotFoundException(USER_NOT_FOUND);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!checkUser(userId)) {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
        userRepository.deleteUser(userId);
    }

    @Override
    public boolean checkUser(Long userId) {
        return userRepository.getUserById(userId) != null;
    }
}
