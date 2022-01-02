package com.bnifp.mufis.postservice.controller;

import com.bnifp.mufis.postservice.dto.input.PostLikeInput;
import com.bnifp.mufis.postservice.dto.response.BaseResponse;
import com.bnifp.mufis.postservice.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/post-likes")
public class PostLikeController extends BaseController {

    @Autowired
    private PostLikeService postLikeService;


    @PostMapping
    public ResponseEntity<BaseResponse> addOne(HttpServletRequest request,
                                               @Valid @RequestBody PostLikeInput input){

//        System.out.println(request.getHeader("Authorization"));
//        System.out.println(request.getHeader("PostId"));

        Long userId = Long.parseLong(request.getHeader("id"));
        String role = request.getHeader("role");

        return postLikeService.addOne(input, userId);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAll(HttpServletRequest request){
        Long postId = Long.parseLong(request.getHeader("PostId"));
        if(!(postId==null)){
            return postLikeService.getAllByPostId(postId);
        }

        String message = "Data not found!";
        return new ResponseEntity<BaseResponse>(new BaseResponse<>
                (Boolean.FALSE, message), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping()
    public ResponseEntity<BaseResponse> deleteByUserIdAndPostId(HttpServletRequest request,
                                                  @Valid @RequestBody PostLikeInput input){

        Long userId = Long.parseLong(request.getHeader("id"));
        Long postId = input.getPostId();
        String role = request.getHeader("role");

        return postLikeService.deleteByUserIdAndPostId(userId, postId);
    }
}
