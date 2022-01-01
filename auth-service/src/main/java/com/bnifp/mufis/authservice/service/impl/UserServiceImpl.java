package com.bnifp.mufis.authservice.service.impl;

import com.bnifp.mufis.authservice.dto.output.UserOutput;
import com.bnifp.mufis.authservice.dto.output.UserOutputDetail;
import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import com.bnifp.mufis.authservice.model.User;
import com.bnifp.mufis.authservice.repository.UserRepository;
import com.bnifp.mufis.authservice.service.KafkaProducer;
import com.bnifp.mufis.authservice.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.apache.commons.collections4.IterableUtils;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaProducer kafkaProducer;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    public void setMapper(ModelMapper mapper){
        this.mapper = mapper;
    }

    public ResponseEntity<BaseResponse> getOne(String username){
        //Error if user not found has been handled by loadUserByUsername
        User user = userRepository.getDistinctTopByUsername(username);
        UserOutputDetail userDetail = this.mapper.map(user, UserOutputDetail.class);
        return ResponseEntity.ok(new BaseResponse<>(userDetail));
    }

    public ResponseEntity<BaseResponse> getAll(){
        Iterable<User> users = userRepository.findAll();
        List<User> userList = IterableUtils.toList(users);
        List<UserOutput> outputs = new ArrayList<>();

        for(User post: userList){
            outputs.add(mapper.map(post, UserOutput.class));
        }
        return new ResponseEntity<BaseResponse>(new BaseResponse<>(outputs), HttpStatus.OK);
    }

    public ResponseEntity<BaseResponse> deleteOne(String username){
        //Error if user not found has been handled by loadUserByUsername
        User user = userRepository.getDistinctTopByUsername(username);
        userRepository.deleteById(user.getId());

        try{
            kafkaProducer.produce(new Gson().toJson(user));
        }catch (Exception e){
            String message = "Successfully Deleted post with id: " + user.getId().toString();
            return ResponseEntity.ok(new BaseResponse<>(Boolean.TRUE, message));
        }

        String message = "Successfully Deleted post with id: " + user.getId().toString();
        return ResponseEntity.ok(new BaseResponse<>(Boolean.TRUE, message));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getDistinctTopByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Username Not Found");

        return user;
    }
}