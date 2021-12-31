package com.bnifp.mufis.authservice.controller;

import com.bnifp.mufis.authservice.dto.input.UserInput;
import com.bnifp.mufis.authservice.dto.input.UserInputLogin;
import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import com.bnifp.mufis.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/auths")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> addOne(@Valid @RequestBody UserInput userInput) {
        return authService.addOne(userInput);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> generateToken(@RequestBody UserInputLogin userInputLogin) {
        return authService.generateToken(userInputLogin);
    }
}
