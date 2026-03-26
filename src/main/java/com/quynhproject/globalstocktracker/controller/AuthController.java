package com.quynhproject.globalstocktracker.controller;

import com.quynhproject.globalstocktracker.domain.dto.request.LoginUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.ApiResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.LoginUserResponse;
import com.quynhproject.globalstocktracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}
