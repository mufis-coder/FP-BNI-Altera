package com.bnifp.mufis.categoryservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="user_categories",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"user_id", "category_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "category_id")
    private Long categoryId;

}
