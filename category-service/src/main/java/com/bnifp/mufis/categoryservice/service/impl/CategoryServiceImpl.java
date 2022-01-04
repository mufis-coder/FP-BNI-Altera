package com.bnifp.mufis.categoryservice.service.impl;

import com.bnifp.mufis.categoryservice.dto.input.CategoryInput;
import com.bnifp.mufis.categoryservice.dto.output.CategoryOutput;
import com.bnifp.mufis.categoryservice.exception.InputNullException;
import com.bnifp.mufis.categoryservice.model.Category;
import com.bnifp.mufis.categoryservice.repository.CategoryRepository;
import com.bnifp.mufis.categoryservice.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

//    @Autowired
//    private KafkaProducer kafkaProducer;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    public void setMapper(ModelMapper mapper){
        this.mapper = mapper;
    }

    //Check if string is null or empty
    private Boolean isNullorEmpty(String str){
        if (str == null || str.isEmpty() || str.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public CategoryOutput addOne(CategoryInput categoryInput) throws InputNullException {
        if(isNullorEmpty(categoryInput.getName())){
            throw new InputNullException("Name cannot be null or empty!");
        }
        Category category = this.mapper.map(categoryInput, Category.class);
        categoryRepository.save(category);
        return this.mapper.map(category, CategoryOutput.class);
    }

}
