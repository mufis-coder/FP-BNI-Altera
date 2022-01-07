package com.bnifp.mufis.postservice.service.impl;

import com.bnifp.mufis.postservice.dto.input.LogInput;
import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.*;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.model.Post;
import com.bnifp.mufis.postservice.model.PostComment;
import com.bnifp.mufis.postservice.model.PostLike;
import com.bnifp.mufis.postservice.repository.PostCommentRepository;
import com.bnifp.mufis.postservice.repository.PostLikeRepository;
import com.bnifp.mufis.postservice.repository.PostRepository;
import com.bnifp.mufis.postservice.service.KafkaProducer;
import com.bnifp.mufis.postservice.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.IterableUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostRepository postRepository;

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
    private Post findPost(Long id){
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return null;
        }
        return post.get();
    }

    @Override
    public ResponseEntity<BaseResponse> addOne(PostInput postInput, Long user_id) {
        if(isNullorEmpty(postInput.getTitle())|| isNullorEmpty(postInput.getContent())){
            String message = "Title and content cannot be null or empty!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.BAD_REQUEST);
        }

        Post post = mapper.map(postInput, Post.class);
        post.setUser_id(user_id);
        try{
            postRepository.save(post);
        } catch(Exception e){
            String message = e.getMessage();
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.CONFLICT);
        }
        return new ResponseEntity<BaseResponse>(new BaseResponse<>
                (this.mapper.map(post, PostOutput.class)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponse> getOne(Long id) {
        Post post = findPost(id);;
        if(Objects.isNull(post)){
            String message = "Post with id: " + id.toString() + " is not Found";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<BaseResponse>(new BaseResponse<>
                (this.mapper.map(post, PostOutput.class)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponse> getOneDetail(String token, Long id){
        Post post = findPost(id);;
        if(Objects.isNull(post)){
            String message = "Post with id: " + id.toString() + " is not Found";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.NOT_FOUND);
        }

        try {
            String url = "http://localhost:9000/users/" + post.getUser_id().toString();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<LogInput> entity = new HttpEntity<>(null, headers);

            String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

            JSONObject userJson = new JSONObject(response);
            UserOutput userObj = new ObjectMapper().readValue(userJson.get("data").toString(),
                    UserOutput.class);

            PostOutputDetail postDetail = this.mapper.map(post, PostOutputDetail.class);
            postDetail.setCreatedBy(userObj);

            Iterable<PostLike> postLikes = postLikeRepository.findByPostId(post.getId());
            List<PostLike> postLikesList = IterableUtils.toList(postLikes);

            Iterable<PostComment> postComments = postCommentRepository.findByPostId(post.getId());
            List<PostComment> postCommentList = IterableUtils.toList(postComments);
            List<PostCommentOutput> outputs = new ArrayList<>();
            for(PostComment postComment: postCommentList){
                outputs.add(mapper.map(postComment, PostCommentOutput.class));
            }

            postDetail.setTotalLike((long) postLikesList.size());
            postDetail.setComments(outputs);

            return new ResponseEntity<BaseResponse>(new BaseResponse<>
                    (postDetail), HttpStatus.OK);
        }catch (JSONException e){
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<BaseResponse>(new BaseResponse<>
                (Boolean.FALSE, "Operation failed"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<BaseResponse> updateOne(Long id, PostInput postInput){
        if(isNullorEmpty(postInput.getTitle())|| isNullorEmpty(postInput.getContent())){
            String message = "Title and content cannot be null or empty!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.BAD_REQUEST);
        }
        Post post = findPost(id);
        if(Objects.isNull(post)){
            String message = "Post with id: " + id.toString() + " is not Found";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.NOT_FOUND);
        }

        this.mapper.map(postInput, post);
        postRepository.save(post);
        return new ResponseEntity<BaseResponse>(new BaseResponse<>
                (this.mapper.map(post, PostOutput.class)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponse> deleteOne(Long id) {
        Post post = findPost(id);
        if(Objects.isNull(post)){
            String message = "Post with id: " + id.toString() + " is not Found";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.NOT_FOUND);
        }
        try{
            String psn = new JSONObject()
                    .put("name", "post-service")
                    .put("data", post)
                    .toString();
            kafkaProducer.produce(psn);
            postRepository.deleteById(id);
        }catch (Exception e){
            return new ResponseEntity<BaseResponse>(new BaseResponse<>
                    (Boolean.FALSE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String message = "Successfully Deleted post with id: " + id;
        return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.TRUE, message), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponse> getAll() {
        Iterable<Post> posts = postRepository.findAll();
        List<Post> postList = IterableUtils.toList(posts);

        List<PostOutput> outputs = new ArrayList<>();
        for(Post post: postList){
            outputs.add(mapper.map(post, PostOutput.class));
        }
        return new ResponseEntity<BaseResponse>(new BaseResponse<>(outputs), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponse> getAllFitCategories(String token){
        try {
            String url = "http://localhost:9000/users/me/detail";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<LogInput> entity = new HttpEntity<>(null, headers);

            String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

            JSONObject UserCategoriesJson = new JSONObject(response);
            UserOutputCategories userCategories = new ObjectMapper().readValue(
                    UserCategoriesJson.get("data").toString(),
                    UserOutputCategories.class);

            List<Long> categoriesId = new ArrayList<>();
            for(CategoryOutput category: userCategories.getCategories()){
                categoriesId.add(category.getId());
            }

            String url2 = "http://localhost:9000/post-categories";

            HttpHeaders headers2 = new HttpHeaders();
            headers2.add("Authorization", token);
            headers2.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<LogInput> entity2 = new HttpEntity<>(null, headers2);

            String response2 = restTemplate.exchange(url2, HttpMethod.GET, entity2, String.class).getBody();

            JSONObject postCategoriesJson = new JSONObject(response2);
            List<PostCategory> userCategoryOutputList = new ObjectMapper().readValue(
                    postCategoriesJson.get("data").toString(), new TypeReference<List<PostCategory>>(){});


            Iterable<Post> posts = postRepository.findAll();
            List<Post> postList = IterableUtils.toList(posts);

            List<PostOutput> outputs = new ArrayList<>();
            for(Post post: postList){

                for(PostCategory postCategory: userCategoryOutputList){
                    if(post.getId() == postCategory.getPostId()){
                        if(categoriesId.contains(postCategory.getCategoryId())){
                            outputs.add(mapper.map(post, PostOutput.class));
                            break;
                        }
                    }
                }

            }

            return new ResponseEntity<BaseResponse>(new BaseResponse<>(outputs), HttpStatus.OK);

        }catch (JSONException e){
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<BaseResponse>(new BaseResponse<>
                (Boolean.FALSE, "Operation failed"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
