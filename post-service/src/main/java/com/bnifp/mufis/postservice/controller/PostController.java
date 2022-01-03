package com.bnifp.mufis.postservice.controller;

import com.bnifp.mufis.postservice.dto.input.LogInput;
import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.PostOutput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.model.Post;
import com.bnifp.mufis.postservice.service.impl.KafkaProducerImpl;
import com.bnifp.mufis.postservice.service.PostService;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<BaseResponse> addOne(HttpServletRequest request,
                                               @Valid @RequestBody PostInput input){

//        String username = request.getHeader("username");
        Long user_id = Long.parseLong(request.getHeader("id"));
        String role = request.getHeader("role");

        if(!(role.equals("ADMIN") || role.equals("TRAINER"))){
            String msg = role + " is not authorized to access this resource!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>
                    (Boolean.FALSE, msg), HttpStatus.FORBIDDEN);
        }
        return postService.addOne(input, user_id);
    }

    @GetMapping({"/{id}"})
    public  ResponseEntity<BaseResponse> getOne(@PathVariable Long id){
        return postService.getOne(id);
    }

    @PatchMapping({"/{id}"})
    public ResponseEntity<BaseResponse> updateOne(HttpServletRequest request, @PathVariable Long id,
                                                              @Valid @RequestBody PostInput input){

        Long user_id = Long.parseLong(request.getHeader("id"));
        String role = request.getHeader("role");

        if(!(role.equals("ADMIN") || role.equals("TRAINER"))){
            String msg = role + " is not authorized to access this resource!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>
                    (Boolean.FALSE, msg), HttpStatus.FORBIDDEN);
        }
        return postService.updateOne(id, input);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteOne(HttpServletRequest request,
                                                  @PathVariable Long id){
//        writeLog(post); //write log to log-service
        Long user_id = Long.parseLong(request.getHeader("id"));
        String role = request.getHeader("role");

        if(!(role.equals("ADMIN") || role.equals("TRAINER"))){
            String msg = role + " is not authorized to access this resource!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>
                    (Boolean.FALSE, msg), HttpStatus.FORBIDDEN);
        }
        return postService.deleteOne(id);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAll(){
        return postService.getAll();
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<BaseResponse> getOneDetail(HttpServletRequest request,
                                                     @PathVariable Long id){

        return postService.getOneDetail(request.getHeader("Authorization"), id);

    }
}
