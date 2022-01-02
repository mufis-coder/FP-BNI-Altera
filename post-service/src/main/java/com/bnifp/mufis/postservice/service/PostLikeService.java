package com.bnifp.mufis.postservice.service;

import com.bnifp.mufis.postservice.dto.input.PostLikeInput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PostLikeService {
    ResponseEntity<BaseResponse> addOne(PostLikeInput input, Long userId);
    ResponseEntity<BaseResponse> deleteByUserIdAndPostId(Long userId, Long postId);

    ResponseEntity<BaseResponse>  getAllByPostId(Long postId);
}
