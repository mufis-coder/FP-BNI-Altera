package com.bnifp.mufis.authservice.service.impl;

import com.bnifp.mufis.authservice.dto.input.UserInput;
import com.bnifp.mufis.authservice.dto.output.UserOutput;
import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import com.bnifp.mufis.authservice.model.User;
import com.bnifp.mufis.authservice.payload.TokenResponse;
import com.bnifp.mufis.authservice.payload.UsernamePassword;
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

    @Override
    public ResponseEntity<BaseResponse> addOne(UserInput userInput) {
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
    public TokenResponse generateToken(UsernamePassword req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getUsername(),
                            req.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwt);
            return tokenResponse;
        } catch (BadCredentialsException e) {
            log.error("Bad Credential", e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
