package com.bnifp.mufis.postservice.repository;

import com.bnifp.mufis.postservice.model.PostLike;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends CrudRepository<PostLike, Long> {
}
