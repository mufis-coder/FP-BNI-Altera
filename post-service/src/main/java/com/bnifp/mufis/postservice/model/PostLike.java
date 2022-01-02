package com.bnifp.mufis.postservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="post_likes",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"user_id", "post_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;
}
