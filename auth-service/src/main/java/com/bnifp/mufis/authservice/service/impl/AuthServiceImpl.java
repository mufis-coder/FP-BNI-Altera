package com.bnifp.mufis.authservice.service.impl;

import com.bnifp.mufis.authservice.dto.input.UserInput;
import com.bnifp.mufis.authservice.dto.input.UserInputLogin;
import com.bnifp.mufis.authservice.dto.output.UserOutput;
import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import com.bnifp.mufis.authservice.model.User;
import com.bnifp.mufis.authservice.dto.response.TokenResponse;
import com.bnifp.mufis.authservice.repository.UserRepository;
import com.bnifp.mufis.authservice.security.JwtTokenProvider;
import com.bnifp.mufis.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public ResponseEntity<BaseResponse> addOne(UserInput userInput) {
        if(isNullorEmpty(userInput.getFullname())|| isNullorEmpty(userInput.getUsername())
                || isNullorEmpty(userInput.getEmail()) || isNullorEmpty(userInput.getPassword())){
            String message = "Full name, username, email, and password cannot be null or empty!";
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message), HttpStatus.BAD_REQUEST);
        }

        User user = this.mapper.map(userInput, User.class);
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));

        try{
            userRepository.save(user);
        } catch(Exception e){
//            String message = "Error! Email or Username has been registered!";
            String message = e.getMessage();
            return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message), HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(new BaseResponse<>(this.mapper.map(userInput, UserOutput.class)));
    }

    @Override
    public ResponseEntity<BaseResponse> generateToken(UserInputLogin userInputLogin) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userInputLogin.getUsername(),
                            userInputLogin.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwt);

            User user = userRepository.getDistinctTopByUsername(userInputLogin.getUsername());
            if (user == null){
                String message = "User not found!";
                return new ResponseEntity<BaseResponse>(new BaseResponse<>(Boolean.FALSE, message),
                        HttpStatus.NOT_FOUND);
            }
            tokenResponse.setId(user.getId());

            return new ResponseEntity<BaseResponse>(new BaseResponse<>(tokenResponse),
                    HttpStatus.OK);

        } catch (BadCredentialsException e) {
            log.error("Bad Credential", e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
