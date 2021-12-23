package com.bnifp.mufis.postservice.controller;

import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.PostOutput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.model.Post;
import com.bnifp.mufis.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/post")
public class PostController extends BaseController {

    @Autowired
    @Qualifier("postServiceImpl")
    private PostService postService;

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
        postService.deleteOne(id);
        String message = "Successfully Deleted post with id: " + id.toString();
        return ResponseEntity.ok(new BaseResponse<>(Boolean.TRUE, message));
    }
}
