package com.bnifp.mufis.postservice.controller;

import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.service.PostService;
import com.bnifp.mufis.postservice.service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    @Qualifier("postServiceImpl")
    private PostService postService;

    @GetMapping
    public String getAll(){
        return "HALOOO";
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> addOne(@Valid @RequestBody PostInput input){
        postService.addOne(input);
        return ResponseEntity.ok(new BaseResponse<>(Boolean.TRUE));
    }
}
