package com.bnifp.mufis.authservice.controller;

import com.bnifp.mufis.authservice.dto.input.UserInputUpdate;
import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import com.bnifp.mufis.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<BaseResponse> getAll() {
        return userService.getAll();
    }

    @GetMapping("/me")
    public ResponseEntity<BaseResponse> getOne(Principal principal) {
        String username = principal.getName();
        return userService.getOne(username);
    }

    @PatchMapping(value="/me")
    public ResponseEntity<BaseResponse> updateOne(Principal principal,
                                                  @Valid @RequestBody UserInputUpdate inputUpdate){
        String username = principal.getName();
        return userService.updateOne(username, inputUpdate);
    }

    @DeleteMapping("/me")
    public ResponseEntity<BaseResponse> deleteOne(Principal principal){
        String username = principal.getName();
        return userService.deleteOne(username);
    }
}