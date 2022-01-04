package com.bnifp.mufis.categoryservice.controller;

import com.bnifp.mufis.categoryservice.dto.input.UserCategoryInput;
import com.bnifp.mufis.categoryservice.dto.response.BaseResponse;
import com.bnifp.mufis.categoryservice.exception.InputNullException;
import com.bnifp.mufis.categoryservice.service.UserCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user-categories")
public class UserCategoryController {
    @Autowired
    private UserCategoryService userCategoryService;

    @PostMapping
    public ResponseEntity<BaseResponse> addOne(HttpServletRequest request,
                                               @Valid @RequestBody UserCategoryInput input){
        Long userId = Long.parseLong(request.getHeader("id"));
        String role = request.getHeader("role");

        try{
            String output = userCategoryService.addOne(input, userId);
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.TRUE, output),
                    HttpStatus.OK);

        } catch (InputNullException e){
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
