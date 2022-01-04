package com.bnifp.mufis.categoryservice.service.impl;

import com.bnifp.mufis.categoryservice.dto.input.UserCategoryInput;
import com.bnifp.mufis.categoryservice.exception.InputNullException;
import com.bnifp.mufis.categoryservice.model.UserCategory;
import com.bnifp.mufis.categoryservice.repository.UserCategoryRepository;
import com.bnifp.mufis.categoryservice.service.UserCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCategoryServiceImpl implements UserCategoryService {
    @Autowired
    private UserCategoryRepository userCategoryRepository;

//    @Autowired
//    private KafkaProducer kafkaProducer;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    public void setMapper(ModelMapper mapper){
        this.mapper = mapper;
    }

    public String addOne(UserCategoryInput input, Long userId) throws InputNullException{
        if(input.getCategoryId() == null){
            throw new InputNullException("Category id cannot be null!");
        }
        UserCategory userCategory = this.mapper.map(input, UserCategory.class);
        userCategory.setUserId(userId);
        userCategoryRepository.save(userCategory);
        return "Operation succes";
    }
}
