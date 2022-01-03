package com.bnifp.mufis.postservice.dto.output;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostLikeOutput {
    private Long id;
    private Long userId;
    private Long postId;
}
