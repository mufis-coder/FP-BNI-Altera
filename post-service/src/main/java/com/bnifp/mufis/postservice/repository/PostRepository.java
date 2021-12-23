package com.bnifp.mufis.postservice.repository;
import com.bnifp.mufis.postservice.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

}
