package com.bnifp.mufis.postservice.dto.output;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostOutput {
    private Long id;
    private String title;
    private String content;
}
