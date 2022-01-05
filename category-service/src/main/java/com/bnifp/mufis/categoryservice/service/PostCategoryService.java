package com.bnifp.mufis.categoryservice.service;

import com.bnifp.mufis.categoryservice.dto.input.PostCategoryInput;
import com.bnifp.mufis.categoryservice.exception.DataNotFoundException;
import com.bnifp.mufis.categoryservice.exception.InputNullException;

public interface PostCategoryService {
    String addOne(PostCategoryInput input) throws InputNullException;
    String deleteOne(Long Id) throws DataNotFoundException;
}
