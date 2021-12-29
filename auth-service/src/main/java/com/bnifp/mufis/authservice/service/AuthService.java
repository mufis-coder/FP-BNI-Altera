package com.bnifp.mufis.authservice.service;

import com.bnifp.mufis.authservice.model.User;
import com.bnifp.mufis.authservice.payload.TokenResponse;
import com.bnifp.mufis.authservice.payload.UsernamePassword;

public interface AuthService {
    User register(UsernamePassword req);
    TokenResponse generateToken(UsernamePassword req);
}
