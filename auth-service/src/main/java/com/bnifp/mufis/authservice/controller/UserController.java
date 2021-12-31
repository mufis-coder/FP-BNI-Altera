package com.bnifp.mufis.authservice.controller;

import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import com.bnifp.mufis.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> listUsers() {
        return ResponseEntity.ok("this is list users");
    }

    @GetMapping("/me")
    public ResponseEntity<BaseResponse> getOne(Principal principal) {
        String username = principal.getName();
        return userService.getOne(username);
    }
}