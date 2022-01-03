package com.bnifp.mufis.postservice.service.impl;

import com.bnifp.mufis.postservice.dto.input.PostCommentInput;
import com.bnifp.mufis.postservice.dto.output.PostCommentOutput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.model.PostComment;
import com.bnifp.mufis.postservice.repository.PostCommentRepository;
import com.bnifp.mufis.postservice.service.KafkaProducer;
import com.bnifp.mufis.postservice.service.PostCommentService;
import org.apache.commons.collections4.IterableUtils;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostCommentServiceImpl implements PostCommentService {
    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

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
    private PostComment findPost(Long id){
        Optional<PostComment> post = postCommentRepository.findById(id);
        if (post.isEmpty()) {
            return null;
        }
        return post.get();
    }

    @Override
    public ResponseEntity<BaseResponse> addOne(PostCommentInput input, Long userId) {
        if(isNullorEmpty(input.getComment()) || input.getPostId() == null){
            String message = "Post id and comment cannot be null or empty!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.BAD_REQUEST);
        }

        PostComment postComment = mapper.map(input, PostComment.class);
        postComment.setUserId(userId);
        try{
            postCommentRepository.save(postComment);
        } catch(Exception e){
            String message = e.getMessage();
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.CONFLICT);
        }
        return new ResponseEntity<BaseResponse>(new BaseResponse<>
                (Boolean.TRUE, "Operation succes"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponse> getOne(Long id) {
        PostComment postComment = findPost(id);;
        if(Objects.isNull(postComment)){
            String message = "Post comment with id: " + id.toString() + " is not Found";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<BaseResponse>(new BaseResponse<>
                (this.mapper.map(postComment, PostCommentOutput.class)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponse> updateOne(Long id, PostCommentInput input, Long userId){
        if(isNullorEmpty(input.getComment()) || input.getPostId() == null){
            String message = "Post id and comment cannot be null or empty!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.BAD_REQUEST);
        }

        PostComment postComment = findPost(id);
        if(Objects.isNull(postComment)){
            String message = "Post comment with id: " + id.toString() + " is not Found";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.NOT_FOUND);
        }
        if(postComment.getUserId() != userId){
            String message = "Forbidden! You are not the owner of this comment!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.BAD_REQUEST);
        }

//        this.mapper.map(input, postComment);
        postComment.setComment(input.getComment());
        postComment.setPostId(input.getPostId());
        postCommentRepository.save(postComment);
        return new ResponseEntity<BaseResponse>(new BaseResponse<>
                (this.mapper.map(postComment, PostCommentOutput.class)), HttpStatus.OK);
    }
//
//    @Override
//    public ResponseEntity<BaseResponse> deleteOne(Long id) {
//        Post post = findPost(id);
//        if(Objects.isNull(post)){
//            String message = "Post with id: " + id.toString() + " is not Found";
//            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
//                    HttpStatus.NOT_FOUND);
//        }
//        try{
//            String psn = new JSONObject()
//                    .put("name", "post-service")
//                    .put("data", post)
//                    .toString();
//            kafkaProducer.produce(psn);
//            postRepository.deleteById(id);
//        }catch (Exception e){
//            return new ResponseEntity<BaseResponse>(new BaseResponse<>
//                    (Boolean.FALSE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        String message = "Successfully Deleted post with id: " + id;
//        return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.TRUE, message), HttpStatus.OK);
//    }
//
//    @Override
//    public ResponseEntity<BaseResponse> getAll() {
//        Iterable<Post> posts = postRepository.findAll();
//        List<Post> postList = IterableUtils.toList(posts);
//
//        List<PostOutputDetail> outputs = new ArrayList<>();
//        for(Post post: postList){
//            outputs.add(mapper.map(post, PostOutputDetail.class));
//        }
//        return new ResponseEntity<BaseResponse>(new BaseResponse<>(outputs), HttpStatus.OK);
//    }
}
