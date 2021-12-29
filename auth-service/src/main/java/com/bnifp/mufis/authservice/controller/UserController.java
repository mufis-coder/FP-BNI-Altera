package com.bnifp.mufis.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public ResponseEntity<?> listUsers() {
        return ResponseEntity.ok("this is list users");
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(Principal principal) {
        return ResponseEntity.ok("this is user info " + principal.getName());
    }
}