package com.bnifp.mufis.authservice.dto.output;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserOutputCategories {
    private Long id;
    private String fullname;
    private String username;
    private String email;
    private String role;
    private List<CategoryOutput> categories;
    private String registerFrom;
    protected Date createdAt;
    protected Date updatedAt;
}
