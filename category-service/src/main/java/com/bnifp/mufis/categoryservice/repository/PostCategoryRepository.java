package com.bnifp.mufis.categoryservice.repository;

import com.bnifp.mufis.categoryservice.model.PostCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCategoryRepository extends CrudRepository<PostCategory, Long> {
    List<PostCategory> findByPostId(Long postId);

    PostCategory findByPostIdAndCategoryId(Long postId, Long categoryId);
}
