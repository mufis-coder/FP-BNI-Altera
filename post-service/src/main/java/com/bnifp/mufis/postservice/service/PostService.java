package com.bnifp.mufis.postservice.service;

import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.PostOutput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.model.Post;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {

    ResponseEntity<BaseResponse> addOne(PostInput input, Long user_id);

    ResponseEntity<BaseResponse> getOne(Long id);

    ResponseEntity<BaseResponse> getOneDetail(String token, Long id);

    ResponseEntity<BaseResponse> updateOne(Long id, PostInput input);

    ResponseEntity<BaseResponse> deleteOne(Long id);

    ResponseEntity<BaseResponse> getAll();

    ResponseEntity<BaseResponse> getAllFitCategories(String token);
}
