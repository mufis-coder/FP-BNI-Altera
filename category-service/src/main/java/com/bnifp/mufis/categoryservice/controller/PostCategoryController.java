package com.bnifp.mufis.categoryservice.controller;

import com.bnifp.mufis.categoryservice.dto.input.PostCategoryInput;
import com.bnifp.mufis.categoryservice.dto.response.BaseResponse;
import com.bnifp.mufis.categoryservice.exception.DataNotFoundException;
import com.bnifp.mufis.categoryservice.exception.InputNullException;
import com.bnifp.mufis.categoryservice.service.PostCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/post-categories")
public class PostCategoryController {
    @Autowired
    private PostCategoryService postCategoryService;

    @PostMapping
    public ResponseEntity<BaseResponse> addOne(HttpServletRequest request,
                                               @Valid @RequestBody PostCategoryInput input){
        try{
            String output = postCategoryService.addOne(input);
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.TRUE, output),
                    HttpStatus.OK);

        } catch (InputNullException e){
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteOne(HttpServletRequest request,
                                                  @PathVariable Long id){
        String role = request.getHeader("role");

        if(!(role.equals("ADMIN"))){
            String msg = role + " is not authorized to access this resource!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>
                    (Boolean.FALSE, msg), HttpStatus.FORBIDDEN);
        }
        try{
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(
                    Boolean.TRUE, postCategoryService.deleteOne(id)), HttpStatus.OK);

        }catch (DataNotFoundException e){
            System.out.println(e.getMessage());
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, e.getMessage()),
                    HttpStatus.NO_CONTENT);
        }
    }
}
