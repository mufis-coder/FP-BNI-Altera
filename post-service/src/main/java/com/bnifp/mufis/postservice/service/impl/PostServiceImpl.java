package com.bnifp.mufis.postservice.service.impl;

import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.PostOutput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.model.Post;
import com.bnifp.mufis.postservice.repository.PostRepository;
import com.bnifp.mufis.postservice.service.PostService;
import org.apache.commons.collections4.IterableUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

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

    //function to find a Post with id
    private Post findPost(Long id){
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return null;
        }
        return post.get();
    }

    @Override
    public PostOutput getOne(Long id) {
        Post temp = findPost(id);
        return this.mapper.map(temp, PostOutput.class);
    }

    @Override
    public List<PostOutput> getAll() {
        Iterable<Post> posts = postRepository.findAll();
        List<Post> postList = IterableUtils.toList(posts);

        List<PostOutput> outputs = new ArrayList<>();
        for(Post post: postList){
            outputs.add(mapper.map(post, PostOutput.class));
        }

        return outputs;
    }

    @Override
    public ResponseEntity<BaseResponse> addOne(PostInput postInput) {
        if(isNullorEmpty(postInput.getTitle())|| isNullorEmpty(postInput.getContent())){
            String message = "Title and content cannot be null or empty!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message), HttpStatus.BAD_REQUEST);
        }

        Post post = mapper.map(postInput, Post.class);
        try{
            postRepository.save(post);
        } catch(Exception e){
            String message = e.getMessage();
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(new BaseResponse<>(this.mapper.map(post, PostOutput.class)));
    }

    @Override
    public PostOutput updateOne(Long id, PostInput input){
        Post temp = findPost(id);
        this.mapper.map(input, temp);
        postRepository.save(temp);
        return this.mapper.map(temp, PostOutput.class);
    }

    @Override
    public Post deleteOne(Long id) {
        Post temp = findPost(id);
        postRepository.deleteById(id);
        return temp;
    }

}
