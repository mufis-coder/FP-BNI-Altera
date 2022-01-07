package com.bnifp.mufis.categoryservice.dto.output;

import com.bnifp.mufis.categoryservice.model.Category;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCategoryOutput {
    private Long id;
    private CategoryOutput category;
}
