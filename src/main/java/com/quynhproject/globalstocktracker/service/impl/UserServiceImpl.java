package com.quynhproject.globalstocktracker.service.impl;

import com.quynhproject.globalstocktracker.constant.AuthProvider;
import com.quynhproject.globalstocktracker.domain.dto.request.CreateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.UpdateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.UserResponse;
import com.quynhproject.globalstocktracker.domain.entity.User;
import com.quynhproject.globalstocktracker.domain.mapper.UserMapper;
import com.quynhproject.globalstocktracker.excetion.AppException;
import com.quynhproject.globalstocktracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements com.quynhproject.globalstocktracker.service.UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public UserResponse register(CreateUserRequest request) {

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if(userOptional.isPresent()){
            throw new AppException("Email đã tồn tại");
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setProviderId(null);
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        userRepository.save(user);


        return userMapper.toCreateUserResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return userMapper.toListUserResponse(userRepository.findAll());
    }

    @Override
    public UserResponse delete(Long id) {
        Optional<User> deleteUser = userRepository.findById(id);

        if(deleteUser.isEmpty()){
            throw new AppException("id này chưa tồn tại người dùng!");
        }

        userRepository.deleteById(id);

        return userMapper.toCreateUserResponse(deleteUser.get());
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest request) {
        Optional<User> updateUser = userRepository.findById(id);

        if(updateUser.isEmpty()){
            throw new AppException("User not found");
        }

        userMapper.updateUser(updateUser.get(), request);
        updateUser.get().setPassword(passwordEncoder.encode(request.getPassword()));
        updateUser.get().setUpdateAt(LocalDateTime.now());

        return userMapper.toCreateUserResponse(userRepository.save(updateUser.get()));

    }
}
