package com.bnifp.mufis.categoryservice.service;

import com.bnifp.mufis.categoryservice.dto.input.UserCategoryInput;
import com.bnifp.mufis.categoryservice.exception.DataNotFoundException;
import com.bnifp.mufis.categoryservice.exception.InputNullException;

public interface UserCategoryService {
    String addOne(UserCategoryInput input, Long userId) throws InputNullException;
    String deleteOne(Long Id) throws DataNotFoundException;
}
