package com.bnifp.mufis.authservice.payload;

import lombok.Data;

@Data
public class UsernamePassword {
    private String username;
    private String password;
}
