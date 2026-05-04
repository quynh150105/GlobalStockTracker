package com.quynhproject.globalstocktracker.service;

import com.nimbusds.jose.JOSEException;
import com.quynhproject.globalstocktracker.domain.dto.request.LoginUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.LogoutRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.RefreshTokenRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.LoginUserResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.RefreshTokenResponse;
import com.quynhproject.globalstocktracker.domain.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;

public interface AuthService {
    String generateToken(User user);

    LoginUserResponse login(LoginUserRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    String generateRefreshToken(String username);

}
