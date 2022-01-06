package com.bnifp.mufis.authservice.service.impl;

import com.bnifp.mufis.authservice.dto.input.UserInputUpdate;
import com.bnifp.mufis.authservice.dto.output.*;
import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import com.bnifp.mufis.authservice.model.User;
import com.bnifp.mufis.authservice.repository.UserRepository;
import com.bnifp.mufis.authservice.service.KafkaProducer;
import com.bnifp.mufis.authservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaProducer kafkaProducer;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    RestTemplate restTemplate;

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

    public ResponseEntity<BaseResponse> getOneDetail(String username, String token){
        //Error if user not found has been handled by loadUserByUsername
//        return null;
        try{
            User user = userRepository.getDistinctTopByUsername(username);
            UserOutputCategories userOutputCategories = this.mapper.map(user, UserOutputCategories.class);

            String url = "http://localhost:9000/user-categories";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<?> entity = new HttpEntity<>(null, headers);

            String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

            JSONObject userCategoryJson = new JSONObject(response);
            List<UserCategoryOutput> userCategoryOutputList = new ObjectMapper().readValue(
                    userCategoryJson.get("data").toString(), new TypeReference<List<UserCategoryOutput>>(){});

            List<CategoryOutput> categoryOutputs = new ArrayList<>();
            for(UserCategoryOutput userCategoryOutput: userCategoryOutputList){
                categoryOutputs.add(userCategoryOutput.getCategory());
            }
            userOutputCategories.setCategories(categoryOutputs);
            return new ResponseEntity<BaseResponse>(new BaseResponse<>
                    (userOutputCategories), HttpStatus.OK);
        }catch (JSONException e){
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<BaseResponse>(new BaseResponse<>
                (Boolean.FALSE, "Operation failed!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<BaseResponse> getOneById(Long id){
        Optional<User> user = userRepository.findById(id);
        UserOutputDetail userDetail = this.mapper.map(user.get(), UserOutputDetail.class);
        return ResponseEntity.ok(new BaseResponse<>(userDetail));
    }

    public ResponseEntity<BaseResponse> getAll(){
        Iterable<User> users = userRepository.findAll();
        List<User> userList = IterableUtils.toList(users);
        List<UserOutputDetail> outputs = new ArrayList<>();

        for(User post: userList){
            outputs.add(mapper.map(post, UserOutputDetail.class));
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