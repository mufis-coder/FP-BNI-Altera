package com.bnifp.mufis.categoryservice.service;

import com.bnifp.mufis.categoryservice.dto.input.UserCategoryInput;
import com.bnifp.mufis.categoryservice.dto.output.CategoryOutput;
import com.bnifp.mufis.categoryservice.dto.output.UserCategoryOutput;
import com.bnifp.mufis.categoryservice.exception.DataNotFoundException;
import com.bnifp.mufis.categoryservice.exception.InputNullException;

import java.util.List;

public interface UserCategoryService {
    String addOne(UserCategoryInput input, Long userId) throws InputNullException;
    String deleteOne(Long Id) throws DataNotFoundException;
    List<UserCategoryOutput> getAllByUserId(Long userId);
}
