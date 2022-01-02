package com.bnifp.mufis.postservice.dto.output;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostOutputDetail {
    private Long id;
    private Long user_id;
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
}
