package com.bnifp.mufis.categoryservice.service;

import com.bnifp.mufis.categoryservice.dto.input.PostCategoryInput;
import com.bnifp.mufis.categoryservice.dto.output.CategoryOutput;
import com.bnifp.mufis.categoryservice.exception.DataNotFoundException;
import com.bnifp.mufis.categoryservice.exception.InputNullException;
import com.bnifp.mufis.categoryservice.model.PostCategory;

import java.util.List;

public interface PostCategoryService {
    String addOne(PostCategoryInput input) throws InputNullException;
    String deleteOne(Long Id) throws DataNotFoundException;
    List<PostCategory> getAll();
}
