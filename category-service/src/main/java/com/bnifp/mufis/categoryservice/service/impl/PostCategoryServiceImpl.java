package com.bnifp.mufis.categoryservice.service.impl;

import com.bnifp.mufis.categoryservice.dto.input.PostCategoryInput;
import com.bnifp.mufis.categoryservice.exception.DataNotFoundException;
import com.bnifp.mufis.categoryservice.exception.InputNullException;
import com.bnifp.mufis.categoryservice.model.PostCategory;
import com.bnifp.mufis.categoryservice.repository.PostCategoryRepository;
import com.bnifp.mufis.categoryservice.service.PostCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PostCategoryServiceImpl implements PostCategoryService {
    @Autowired
    private PostCategoryRepository postCategoryRepository;

//    @Autowired
//    private KafkaProducer kafkaProducer;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    public void setMapper(ModelMapper mapper){
        this.mapper = mapper;
    }

    //function to find a Post with id
    private PostCategory findUserCategory(Long id){
        Optional<PostCategory> postCategory = postCategoryRepository.findById(id);
        if (postCategory.isEmpty()) {
            return null;
        }
        return postCategory.get();
    }

    public String addOne(PostCategoryInput input) throws InputNullException{
        if(input.getCategoryId() == null || input.getPostId() == null){
            throw new InputNullException("Post id and category id cannot be null!");
        }
        PostCategory postCategory = new PostCategory();
        postCategory.setCategoryId(input.getCategoryId());
        postCategory.setPostId(input.getPostId());
        postCategoryRepository.save(postCategory);
        return "Operation succes";
    }

    public String deleteOne(Long id) throws DataNotFoundException{
        PostCategory postCategory = findUserCategory(id);;
        if(Objects.isNull(postCategory)){
            String message = "Post category with id: " + id.toString() + " is not Found";
            throw new DataNotFoundException(message);
        }
        postCategoryRepository.deleteById(id);
        String message = "Successfully deleted post-category with id: " + id;
        return message;
    }
}
