package com.bnifp.mufis.postservice.controller;

import com.bnifp.mufis.postservice.dto.input.PostCommentInput;
import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.service.PostCommentService;
import com.bnifp.mufis.postservice.service.impl.KafkaProducerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/post-comments")
public class PostCommentController {

    @Autowired
    private PostCommentService postCommentService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private KafkaProducerImpl producer;

    @PostMapping
    public ResponseEntity<BaseResponse> addOne(HttpServletRequest request,
                                               @Valid @RequestBody PostCommentInput input){
        Long userId = Long.parseLong(request.getHeader("id"));
        return postCommentService.addOne(input, userId);
    }

    @GetMapping({"/{id}"})
    public  ResponseEntity<BaseResponse> getOne(@PathVariable Long id){
        return postCommentService.getOne(id);
    }

    @PatchMapping({"/{id}"})
    public ResponseEntity<BaseResponse> updateOne(HttpServletRequest request, @PathVariable Long id,
                                                  @Valid @RequestBody PostCommentInput input){

        Long userId = Long.parseLong(request.getHeader("id"));
        String role = request.getHeader("role");

        return postCommentService.updateOne(id, input, userId);
    }

//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseResponse> deleteOne(HttpServletRequest request,
//                                                  @PathVariable Long id){
////        writeLog(post); //write log to log-service
//        Long user_id = Long.parseLong(request.getHeader("id"));
//        String role = request.getHeader("role");
//
//        if(!(role.equals("ADMIN") || role.equals("TRAINER"))){
//            String msg = role + " is not authorized to access this resource!";
//            return new ResponseEntity<BaseResponse>(new BaseResponse<>
//                    (Boolean.FALSE, msg), HttpStatus.FORBIDDEN);
//        }
//        return postService.deleteOne(id);
//    }
//
//    @GetMapping
//    public ResponseEntity<BaseResponse> getAll(){
//        return postService.getAll();
//    }
}
