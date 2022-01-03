package com.bnifp.mufis.postservice.dto.output;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCommentOutput {
    private Long id;
    private Long userId;
//    private Long postId;
    private String comment;
    protected Date createdAt;
    protected Date updatedAt;
}
