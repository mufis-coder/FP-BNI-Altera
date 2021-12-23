package com.bnifp.mufis.postservice.controller;

import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.PostOutput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController extends BaseController {

    @Autowired
    @Qualifier("postServiceImpl")
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostOutput>> getAll(){
        return ResponseEntity.ok(postService.getAll());
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> addOne(@Valid @RequestBody PostInput input){
        postService.addOne(input);
        return ResponseEntity.ok(new BaseResponse<>(Boolean.TRUE));
    }
}
