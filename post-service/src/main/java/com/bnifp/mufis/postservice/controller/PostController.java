package com.bnifp.mufis.postservice.controller;

import com.bnifp.mufis.postservice.dto.input.LogInput;
import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.PostOutput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.model.Post;
import com.bnifp.mufis.postservice.service.PostService;
import com.google.gson.Gson;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/post")
public class PostController extends BaseController {

    @Autowired
    @Qualifier("postServiceImpl")
    private PostService postService;

    @Autowired
    RestTemplate restTemplate;

    //function for write log to log-service
    private void writeLog(Post input){
        String url = "http://localhost:8085/logs";
        LogInput logInput = new LogInput();
        logInput.setName("post-service");
        logInput.setData(input);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<LogInput> entity = new HttpEntity<>(logInput, headers);

       restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
    }

    @GetMapping({"/{id}"})
    public  ResponseEntity<BaseResponse<PostOutput>> getOne(@PathVariable Long id){
        PostOutput output = postService.getOne(id);
        if(Objects.isNull(output)){
            String message = "Post with id: " + id.toString() + " is not Found";
            return ResponseEntity.ok(new BaseResponse<>(Boolean.FALSE, message));
        }
        return ResponseEntity.ok(new BaseResponse<>(output));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<PostOutput>>> getAll(){
        return ResponseEntity.ok( new BaseResponse(postService.getAll()) );
    }

    @PostMapping
    public ResponseEntity<BaseResponse<PostOutput>> addOne(@Valid @RequestBody PostInput input){
        PostOutput output = postService.addOne(input);
        return ResponseEntity.ok(new BaseResponse<>(output));
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<BaseResponse<PostOutput>> updateOne(@PathVariable Long id,
                                                              @Valid @RequestBody PostInput input){
        PostOutput output = postService.updateOne(id, input);
        if(Objects.isNull(output)){
            String message = "Post with id: " + id.toString() + " is not Found";
            return ResponseEntity.ok(new BaseResponse<>(Boolean.FALSE, message));
        }
        return ResponseEntity.ok(new BaseResponse<>(output));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOne(@PathVariable Long id){
        Post post = postService.deleteOne(id);
        writeLog(post); //write log to log-service
        String message = "Successfully Deleted post with id: " + id.toString();
        return ResponseEntity.ok(new BaseResponse<>(Boolean.TRUE, message));
    }
}
