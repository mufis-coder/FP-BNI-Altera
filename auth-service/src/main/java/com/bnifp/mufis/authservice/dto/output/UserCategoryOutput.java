package com.bnifp.mufis.authservice.dto.output;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCategoryOutput {
    private Long id;
    private CategoryOutput category;
}
