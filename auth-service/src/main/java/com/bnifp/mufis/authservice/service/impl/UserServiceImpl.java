package com.bnifp.mufis.authservice.service.impl;

import com.bnifp.mufis.authservice.dto.input.UserInputUpdate;
import com.bnifp.mufis.authservice.dto.output.UserOutput;
import com.bnifp.mufis.authservice.dto.output.UserOutputDetail;
import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import com.bnifp.mufis.authservice.model.User;
import com.bnifp.mufis.authservice.repository.UserRepository;
import com.bnifp.mufis.authservice.service.KafkaProducer;
import com.bnifp.mufis.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.collections4.IterableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaProducer kafkaProducer;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    public void setMapper(ModelMapper mapper){
        this.mapper = mapper;
    }

    //Check if string is null or empty
    private Boolean isNullorEmpty(String str){
        if (str == null || str.isEmpty() || str.trim().isEmpty()) {
            return true;
        }
        return false;
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
            String psn = new JSONObject()
                    .put("name", "auth-service")
                    .put("data", user)
                    .toString();
            kafkaProducer.produce(psn);
        }catch (Exception e){
            String message = "Successfully Deleted post with id: " + user.getId().toString();
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.TRUE, message), HttpStatus.OK);
        }

        String message = "Successfully Deleted post with id: " + user.getId().toString();
        return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.TRUE, message), HttpStatus.OK);
    }

    public ResponseEntity<BaseResponse> updateOne(String username, UserInputUpdate inputUpdate){
        //Error if user not found has been handled by loadUserByUsername
        User user = userRepository.getDistinctTopByUsername(username);

        if(Objects.isNull(user)){
            String message = "User with username: " + username + " is not Found";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.NOT_FOUND);
        }

        String password = user.getPassword();
        this.mapper.map(inputUpdate, user);

        //if user not change the password
        user.setPassword(password);

        password = inputUpdate.getPassword();

        if(!isNullorEmpty(password)){
            user.setPassword(new BCryptPasswordEncoder().encode(password));
        }
        try{
            userRepository.save(user);
        }catch (Exception e){
            String message = e.getMessage();
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<BaseResponse>(new BaseResponse<>(this.mapper.map(user, UserOutputDetail.class))
                , HttpStatus.OK);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getDistinctTopByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Username Not Found");

        return user;
    }
}