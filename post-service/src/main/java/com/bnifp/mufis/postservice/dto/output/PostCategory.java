package com.bnifp.mufis.postservice.dto.output;

import lombok.*;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCategory {
    private Long id;
    private Long postId;
    private Long categoryId;
}
