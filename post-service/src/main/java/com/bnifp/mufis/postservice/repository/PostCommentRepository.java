package com.bnifp.mufis.postservice.repository;

import com.bnifp.mufis.postservice.model.PostComment;
import com.bnifp.mufis.postservice.model.PostLike;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends CrudRepository<PostComment, Long> {

    PostComment findByUserIdAndPostId(Long userId, Long postId);

    List<PostComment> findByPostId(Long postId);
}
