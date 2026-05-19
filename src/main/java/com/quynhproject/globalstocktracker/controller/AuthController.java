package com.quynhproject.globalstocktracker.controller;

import com.nimbusds.jose.JOSEException;
import com.quynhproject.globalstocktracker.domain.dto.request.LoginUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.LogoutRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.RefreshTokenRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.ApiResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.LoginUserResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.RefreshTokenResponse;
import com.quynhproject.globalstocktracker.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and token APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Login successful"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Bad Request"
            ),

    })
    @Operation(summary = "Login Api")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginUserRequest request){
       return ResponseEntity.status(HttpStatus.OK).body(
               ApiResponse.<LoginUserResponse>builder()
                       .message("Login successful")
                       .status(HttpStatus.OK.value())
                       .data(authService.login(request))
                       .build()
       );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<?>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<RefreshTokenResponse>builder()
                        .message("Refresh token successful")
                        .status(HttpStatus.OK.value())
                        .data(authService.refreshToken(request))
                        .build()
        );
    }

    @PostMapping("/logout")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "logout successful"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Bad Request"
            ),

    })
    @Operation(summary = "Logout Api")
    public ResponseEntity<ApiResponse<String>> logout(@Valid @RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<String>builder()
                .data("Logout successful")
                .message("Logout successful")
                .status(HttpStatus.OK.value())
                .build());
    }



}
