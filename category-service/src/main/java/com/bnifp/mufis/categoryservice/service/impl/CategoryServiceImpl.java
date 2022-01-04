package com.bnifp.mufis.categoryservice.service.impl;

import com.bnifp.mufis.categoryservice.dto.input.CategoryInput;
import com.bnifp.mufis.categoryservice.dto.output.CategoryOutput;
import com.bnifp.mufis.categoryservice.exception.InputNullException;
import com.bnifp.mufis.categoryservice.model.Category;
import com.bnifp.mufis.categoryservice.repository.CategoryRepository;
import com.bnifp.mufis.categoryservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

//    @Autowired
//    private KafkaProducer kafkaProducer;

    @Autowired
    private ModelMapper mapper;

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
        Category category = mapper.map(categoryInput, Category.class);
        categoryRepository.save(category);
        return mapper.map(category, CategoryOutput.class);
    }

}
