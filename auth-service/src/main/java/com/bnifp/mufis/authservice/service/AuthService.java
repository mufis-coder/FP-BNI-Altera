package com.bnifp.mufis.authservice.service;

import com.bnifp.mufis.authservice.dto.input.UserInput;
import com.bnifp.mufis.authservice.dto.input.UserInputLogin;
import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<BaseResponse> addOne (UserInput userInput);
    ResponseEntity<BaseResponse> generateToken(UserInputLogin userInputLogin);
}
