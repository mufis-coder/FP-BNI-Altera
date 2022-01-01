package com.bnifp.mufis.postservice.service;

import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.PostOutput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.model.Post;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {
    PostOutput getOne(Long id);

    List<PostOutput> getAll();

    ResponseEntity<BaseResponse> addOne(PostInput input);

    PostOutput updateOne(Long id, PostInput input);

    Post deleteOne(Long id);
}
