package com.bnifp.mufis.postservice.dto.output;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostOutputDetail {
    private Long id;
    private UserOutput createdBy;
    private String title;
    private String content;
    private Long totalLike;
    private List<PostCommentOutput> comments;
    private Date createdAt;
    private Date updatedAt;
}
