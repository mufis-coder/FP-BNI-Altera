package com.bnifp.mufis.postservice.service;

import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.PostOutput;
import com.bnifp.mufis.postservice.model.Post;

import java.util.List;

public interface PostService {
    PostOutput getOne(Long id);

    List<PostOutput> getAll();

    PostOutput addOne(PostInput input);

    PostOutput updateOne(Long id, PostInput input);

    Post deleteOne(Long id);
}
