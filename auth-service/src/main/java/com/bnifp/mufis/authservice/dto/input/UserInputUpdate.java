package com.bnifp.mufis.authservice.dto.input;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInputUpdate {
    private String fullname;
    private String username;
    private String email;
    private String password;
}
