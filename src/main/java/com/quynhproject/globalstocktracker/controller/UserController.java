package com.quynhproject.globalstocktracker.controller;

import com.quynhproject.globalstocktracker.domain.dto.request.CreateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.UpdateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.ApiResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.UserResponse;
import com.quynhproject.globalstocktracker.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
@Tag(name = "Users", description = "User registration and management APIs")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<UserResponse>builder()
                        .data(userService.register(createUserRequest))
                        .message("User created")
                        .status(HttpStatus.CREATED.value())
                        .build());
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> getAllUser(){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<UserResponse>>builder()
                        .message("List users")
                        .data(userService.getAll())
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<UserResponse>builder()
                        .message("User deleted")
                        .status(HttpStatus.OK.value())
                        .data(userService.delete(id))
                        .build()

        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateLocalUser(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<UserResponse>builder()
                        .message("User updated")
                        .status(HttpStatus.OK.value())
                        .data(userService.update(id, request))
                        .build()
        );
    }


}
