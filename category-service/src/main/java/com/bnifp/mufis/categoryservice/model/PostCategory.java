package com.bnifp.mufis.categoryservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="post_categories",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"post_id", "category_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "category_id")
    private Long categoryId;
}
