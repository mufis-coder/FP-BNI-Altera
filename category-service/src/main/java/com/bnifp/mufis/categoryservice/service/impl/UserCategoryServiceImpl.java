package com.bnifp.mufis.categoryservice.service.impl;

import com.bnifp.mufis.categoryservice.dto.input.UserCategoryInput;
import com.bnifp.mufis.categoryservice.exception.DataNotFoundException;
import com.bnifp.mufis.categoryservice.exception.InputNullException;
import com.bnifp.mufis.categoryservice.model.Category;
import com.bnifp.mufis.categoryservice.model.UserCategory;
import com.bnifp.mufis.categoryservice.repository.UserCategoryRepository;
import com.bnifp.mufis.categoryservice.service.UserCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

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

    //function to find a Post with id
    private UserCategory findUserCategory(Long id){
        Optional<UserCategory> userCategory = userCategoryRepository.findById(id);
        if (userCategory.isEmpty()) {
            return null;
        }
        return userCategory.get();
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

    public String deleteOne(Long id) throws DataNotFoundException{
        UserCategory userCategory = findUserCategory(id);;
        if(Objects.isNull(userCategory)){
            String message = "User category with id: " + id.toString() + " is not Found";
            throw new DataNotFoundException(message);
        }
        userCategoryRepository.deleteById(id);
        String message = "Successfully deleted user-category with id: " + id;
        return message;
    }
}
