package com.bnifp.mufis.authservice.dto.input;

import lombok.Data;

@Data
public class UserInputLogin {
    private String username;
    private String password;
}
