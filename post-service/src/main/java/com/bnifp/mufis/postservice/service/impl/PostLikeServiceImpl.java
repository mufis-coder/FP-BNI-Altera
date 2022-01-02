package com.bnifp.mufis.postservice.service.impl;

import com.bnifp.mufis.postservice.dto.input.PostLikeInput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.model.PostLike;
import com.bnifp.mufis.postservice.repository.PostLikeRepository;
import com.bnifp.mufis.postservice.service.KafkaProducer;
import com.bnifp.mufis.postservice.service.PostLikeService;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PostLikeServiceImpl implements PostLikeService {
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    public void setMapper(ModelMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<BaseResponse> addOne(PostLikeInput postLikeInput, Long userId) {
        if(postLikeInput.getPostId() == null){
            String message = "Post id is null!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.BAD_REQUEST);
        }

        PostLike postLike = mapper.map(postLikeInput, PostLike.class);
        postLike.setUserId(userId);

        try{
            postLikeRepository.save(postLike);
        } catch(Exception e){
            String message = e.getMessage();
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.CONFLICT);
        }
        String message = "Operation success!";
        return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.TRUE, message), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponse> deleteByUserIdAndPostId(Long userId, Long postId){
        PostLike postLike  = postLikeRepository.findByUserIdAndPostId(userId, postId);

        if(Objects.isNull(postLike)){
            String message = "Post with user id: " + userId + " and post id: " + postId + " is not Found";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.NOT_FOUND);
        }

        try{
            String psn = new JSONObject()
                    .put("name", "post-like-service")
                    .put("data", postLike)
                    .toString();
            kafkaProducer.produce(psn);
            postLikeRepository.deleteById(postLike.getId());
        }catch (Exception e){
            return new ResponseEntity<BaseResponse>(new BaseResponse<>
                    (Boolean.FALSE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String message = "Successfully deleted post with user id: " + userId + " and post id: " + postId;
        return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.TRUE, message), HttpStatus.OK);
    }

}
