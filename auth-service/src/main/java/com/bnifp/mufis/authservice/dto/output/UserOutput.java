package com.bnifp.mufis.authservice.dto.output;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserOutput {
    private String fullname;
    private String username;
    private String email;
    private String role;
}
