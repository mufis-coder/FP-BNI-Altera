package com.bnifp.mufis.authservice.service;

import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    ResponseEntity<BaseResponse> getOne(String username);
    ResponseEntity<BaseResponse> getAll();
}
