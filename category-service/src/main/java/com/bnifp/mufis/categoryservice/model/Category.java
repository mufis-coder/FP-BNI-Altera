package com.bnifp.mufis.categoryservice.model;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name="categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "created_at")
    @CreationTimestamp
    protected Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    protected Date updatedAt;
}
