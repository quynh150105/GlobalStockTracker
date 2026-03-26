package com.quynhproject.globalstocktracker.service;

import com.quynhproject.globalstocktracker.domain.dto.request.CreateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.UpdateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse register(CreateUserRequest request);

    List<UserResponse> getAll();

    UserResponse delete(Long id);

    UserResponse update(Long id , UpdateUserRequest request);
}
