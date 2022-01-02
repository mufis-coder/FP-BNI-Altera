package com.bnifp.mufis.postservice.controller;

import com.bnifp.mufis.postservice.dto.input.LogInput;
import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.PostOutput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.model.Post;
import com.bnifp.mufis.postservice.service.impl.KafkaProducerImpl;
import com.bnifp.mufis.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController extends BaseController {

    @Autowired
    @Qualifier("postServiceImpl")
    private PostService postService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private KafkaProducerImpl producer;

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

    @PostMapping
    public ResponseEntity<BaseResponse> addOne(@Valid @RequestBody PostInput input){
        return postService.addOne(input);
    }

    @GetMapping({"/{id}"})
    public  ResponseEntity<BaseResponse> getOne(@PathVariable Long id){
        return postService.getOne(id);
    }


    @PatchMapping({"/{id}"})
    public ResponseEntity<BaseResponse> updateOne(@PathVariable Long id,
                                                              @Valid @RequestBody PostInput input){
        return postService.updateOne(id, input);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse>  deleteOne(@PathVariable Long id){
//        writeLog(post); //write log to log-service
        return postService.deleteOne(id);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<PostOutput>>> getAll(){
        return ResponseEntity.ok( new BaseResponse(postService.getAll()) );
    }
}
