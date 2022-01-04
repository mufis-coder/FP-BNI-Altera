package com.bnifp.mufis.categoryservice.service;

import com.bnifp.mufis.categoryservice.dto.input.CategoryInput;
import com.bnifp.mufis.categoryservice.dto.output.CategoryOutput;
import com.bnifp.mufis.categoryservice.exception.InputNullException;

public interface CategoryService {
    CategoryOutput addOne(CategoryInput categoryInput) throws InputNullException;


//    ResponseEntity<BaseResponse> getOne(Long id);
//
//    ResponseEntity<BaseResponse> getOneDetail(String token, Long id);
//
//    ResponseEntity<BaseResponse> updateOne(Long id, PostInput input);
//
//    ResponseEntity<BaseResponse> deleteOne(Long id);
//
//    ResponseEntity<BaseResponse> getAll();
}
