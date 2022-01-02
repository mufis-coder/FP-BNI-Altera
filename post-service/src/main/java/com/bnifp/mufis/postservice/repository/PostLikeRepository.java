package com.bnifp.mufis.postservice.repository;

import com.bnifp.mufis.postservice.model.PostLike;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends CrudRepository<PostLike, Long> {

    PostLike findByUserIdAndPostId(Long userId, Long postId);

    List<PostLike> findByPostId(Long postId);
}
