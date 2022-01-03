package com.bnifp.mufis.postservice.dto.input;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCommentInput {
    private Long postId;
    private String comment;
}
