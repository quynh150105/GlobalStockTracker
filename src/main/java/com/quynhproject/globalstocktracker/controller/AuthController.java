package com.quynhproject.globalstocktracker.controller;

import com.nimbusds.jose.JOSEException;
import com.quynhproject.globalstocktracker.domain.dto.request.LoginUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.LogoutRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.RefreshTokenRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.ApiResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.LoginUserResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.RefreshTokenResponse;
import com.quynhproject.globalstocktracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginUserRequest request){
       return ResponseEntity.ok(
               ApiResponse.<LoginUserResponse>builder()
                       .message("Login")
                       .HttpStatus(HttpStatus.ACCEPTED.value())
                       .data(authService.login(request))
                       .build()
       );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<?>> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        return ResponseEntity.ok(
                ApiResponse.<RefreshTokenResponse>builder()
                        .message("Refresh Token")
                        .HttpStatus(HttpStatus.ACCEPTED.value())
                        .data(authService.refreshToken(request))
                        .build()
        );
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        return ApiResponse.<String>builder()
                .data("Logout successful")
                .message("Logout")
                .HttpStatus(HttpStatus.OK.value())
                .build();
    }



}
