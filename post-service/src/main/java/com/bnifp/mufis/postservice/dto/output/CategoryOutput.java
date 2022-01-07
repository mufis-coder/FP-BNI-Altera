package com.bnifp.mufis.postservice.dto.output;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryOutput {
    private Long id;
    private String name;
    private Date createdAt;
    private Date updatedAt;
}