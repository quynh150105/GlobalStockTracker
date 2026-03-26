package com.quynhproject.globalstocktracker.service;

import com.quynhproject.globalstocktracker.domain.dto.request.LoginUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.LoginUserResponse;

public interface AuthService {
    String generateToken(String email);

    LoginUserResponse login(LoginUserRequest request);



}
