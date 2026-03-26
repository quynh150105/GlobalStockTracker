package com.quynhproject.globalstocktracker.controller;

import com.quynhproject.globalstocktracker.domain.dto.request.CreateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.UpdateUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.ApiResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.UserResponse;
import com.quynhproject.globalstocktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .data(userService.register(createUserRequest))
                        .message("created")
                        .HttpStatus(HttpStatus.CREATED.value())
                        .build());
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> getAllUser(){
        return ResponseEntity.ok(
                ApiResponse.<List<UserResponse>>builder()
                        .message("List User")
                        .data(userService.getAll())
                        .HttpStatus(HttpStatus.ACCEPTED.value())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .message("Delete User")
                        .HttpStatus(HttpStatus.ACCEPTED.value())
                        .data(userService.delete(id))
                        .build()

        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateLocalUser(@PathVariable("id") Long id, @RequestBody UpdateUserRequest request){
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .message("Update user")
                        .HttpStatus(HttpStatus.ACCEPTED.value())
                        .data(userService.update(id, request))
                        .build()
        );
    }


}
