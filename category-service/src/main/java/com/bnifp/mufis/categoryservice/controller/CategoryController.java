package com.bnifp.mufis.categoryservice.controller;

import com.bnifp.mufis.categoryservice.dto.input.CategoryInput;
import com.bnifp.mufis.categoryservice.dto.output.CategoryOutput;
import com.bnifp.mufis.categoryservice.dto.response.BaseResponse;
import com.bnifp.mufis.categoryservice.exception.DataNotFoundException;
import com.bnifp.mufis.categoryservice.exception.InputNullException;
import com.bnifp.mufis.categoryservice.model.Category;
import com.bnifp.mufis.categoryservice.service.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController extends BaseController {
    @Autowired
    private CategoryService categoryService;

//    @Autowired
//    RestTemplate restTemplate;

//    @Autowired
//    private KafkaProducerImpl producer;

    @PostMapping
    public ResponseEntity<BaseResponse> addOne(HttpServletRequest request,
                                               @Valid @RequestBody CategoryInput input){
        String role = request.getHeader("role");

        if(!(role.equals("ADMIN"))){
            String msg = role + " is not authorized to access this resource!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>
                    (Boolean.FALSE, msg), HttpStatus.FORBIDDEN);
        }

        try{
            CategoryOutput categoryOutput = categoryService.addOne(input);
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(categoryOutput),
                    HttpStatus.OK);

        } catch (InputNullException e){
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping({"/{id}"})
    public  ResponseEntity<BaseResponse> getOne(@PathVariable Long id){
        try{
            CategoryOutput categoryOutput = categoryService.getOne(id);
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(categoryOutput),
                    HttpStatus.OK);

        } catch (DataNotFoundException e){
            System.out.println(e.getMessage());
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, e.getMessage()),
                    HttpStatus.NO_CONTENT);
        }
    }

    @PatchMapping({"/{id}"})
    public ResponseEntity<BaseResponse> updateOne(HttpServletRequest request, @PathVariable Long id,
                                                  @Valid @RequestBody CategoryInput input){

        String role = request.getHeader("role");

        if(!(role.equals("ADMIN"))){
            String msg = role + " is not authorized to access this resource!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>
                    (Boolean.FALSE, msg), HttpStatus.FORBIDDEN);
        }

        try{
            CategoryOutput categoryOutput = categoryService.updateOne(id, input);
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(categoryOutput),
                    HttpStatus.OK);

        }catch (InputNullException e){
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }catch (DataNotFoundException e){
            System.out.println(e.getMessage());
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, e.getMessage()),
                    HttpStatus.NO_CONTENT);
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
                    Boolean.TRUE, categoryService.deleteOne(id)), HttpStatus.OK);

        }catch (DataNotFoundException e){
            System.out.println(e.getMessage());
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, e.getMessage()),
                    HttpStatus.NO_CONTENT);
        }

    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAll(){
        List<CategoryOutput> categoryOutputList = categoryService.getAll();
        return new ResponseEntity<BaseResponse>(new BaseResponse<>(categoryOutputList), HttpStatus.OK);
    }

}
