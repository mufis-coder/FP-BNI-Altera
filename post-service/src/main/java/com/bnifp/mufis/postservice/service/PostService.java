package com.bnifp.mufis.postservice.service;

import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.PostOutput;

import java.util.List;

public interface PostService {
    PostOutput getOne(Long id);

    List<PostOutput> getAll();

    void addOne(PostInput input);

    void deleteOne(Long id);
}