package com.bnifp.mufis.categoryservice.service.impl;

import com.bnifp.mufis.categoryservice.dto.input.CategoryInput;
import com.bnifp.mufis.categoryservice.dto.output.CategoryOutput;
import com.bnifp.mufis.categoryservice.exception.DataNotFoundException;
import com.bnifp.mufis.categoryservice.exception.InputNullException;
import com.bnifp.mufis.categoryservice.model.Category;
import com.bnifp.mufis.categoryservice.repository.CategoryRepository;
import com.bnifp.mufis.categoryservice.service.CategoryService;
import org.apache.commons.collections4.IterableUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    //function to find a Post with id
    private Category findCategory(Long id){
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            return null;
        }
        return category.get();
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

    @Override
    public CategoryOutput getOne(Long id) throws DataNotFoundException {
        Category category = findCategory(id);;
        if(Objects.isNull(category)){
            String message = "Category with id: " + id.toString() + " is not Found";
            throw new DataNotFoundException(message);
        }

        return this.mapper.map(category, CategoryOutput.class);
    }

    @Override
    public CategoryOutput updateOne(Long id, CategoryInput categoryInput) throws
            InputNullException, DataNotFoundException {
        if(isNullorEmpty(categoryInput.getName())){
            throw new InputNullException("Name cannot be null or empty!");
        }

        Category category = findCategory(id);;
        if(Objects.isNull(category)){
            String message = "Category with id: " + id.toString() + " is not Found";
            throw new DataNotFoundException(message);
        }

        this.mapper.map(categoryInput, category);
        categoryRepository.save(category);
        return this.mapper.map(category, CategoryOutput.class);
    }

    @Override
    public String deleteOne(Long id) throws DataNotFoundException{
        Category category = findCategory(id);;
        if(Objects.isNull(category)){
            String message = "Category with id: " + id.toString() + " is not Found";
            throw new DataNotFoundException(message);
        }
        categoryRepository.deleteById(id);
        String message = "Successfully deleted category with id: " + id;
        return message;
    }

    @Override
    public List<CategoryOutput> getAll(){
        Iterable<Category> category = categoryRepository.findAll();
        List<Category> categoryList = IterableUtils.toList(category);

        List<CategoryOutput> outputs = new ArrayList<>();
        for(Category categor: categoryList){
            outputs.add(mapper.map(categor, CategoryOutput.class));
        }
        return outputs;
    }
}
