package com.bnifp.mufis.categoryservice.dto.input;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCategoryInput {
    private Long categoryId;
}
