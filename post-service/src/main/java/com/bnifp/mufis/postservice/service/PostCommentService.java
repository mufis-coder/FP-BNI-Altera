package com.bnifp.mufis.postservice.service;

import com.bnifp.mufis.postservice.dto.input.PostCommentInput;
import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PostCommentService {
    ResponseEntity<BaseResponse> addOne(PostCommentInput input, Long userId);

    ResponseEntity<BaseResponse> getOne(Long id);
//
    ResponseEntity<BaseResponse> updateOne(Long id, PostCommentInput input, Long userId);
//
//    ResponseEntity<BaseResponse> deleteOne(Long id);
//
    ResponseEntity<BaseResponse> getAll();
}
