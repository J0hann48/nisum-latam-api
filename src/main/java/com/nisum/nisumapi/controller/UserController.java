package com.nisum.nisumapi.controller;

import com.nisum.nisumapi.controller.user.UserRequest;
import com.nisum.nisumapi.controller.user.UserResponse;
import com.nisum.nisumapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) throws Exception {
        UserResponse userResponse = userService.createUser(userRequest);
        if (userResponse == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("edit")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest){
        UserResponse userResponse = userService.updateUser(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

}
