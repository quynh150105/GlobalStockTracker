package com.quynhproject.globalstocktracker.service.impl;

import com.quynhproject.globalstocktracker.constant.AuthProvider;
import com.quynhproject.globalstocktracker.domain.dto.request.CreateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.UpdateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.UserResponse;
import com.quynhproject.globalstocktracker.domain.entity.User;
import com.quynhproject.globalstocktracker.domain.mapper.UserMapper;
import com.quynhproject.globalstocktracker.excetion.AppException;
import com.quynhproject.globalstocktracker.repository.UserRepository;
import com.quynhproject.globalstocktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public UserResponse register(CreateUserRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if(userOptional.isPresent()){
            throw new AppException("Email already exists");
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setProviderId(null);
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return userMapper.toCreateUserResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return userMapper.toListUserResponse(userRepository.findAll());
    }

    @Override
    public UserResponse delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found"));

        userRepository.delete(user);

        return userMapper.toCreateUserResponse(user);
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found"));

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userMapper.toCreateUserResponse(userRepository.save(user));
    }
}
