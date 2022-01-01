package com.bnifp.mufis.authservice.service;

import com.bnifp.mufis.authservice.dto.input.UserInputUpdate;
import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    ResponseEntity<BaseResponse> getOne(String username);
    ResponseEntity<BaseResponse> getAll();
    ResponseEntity<BaseResponse> deleteOne(String username);
    ResponseEntity<BaseResponse> updateOne(String username, UserInputUpdate inputUpdate);
}
