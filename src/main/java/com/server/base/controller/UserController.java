package com.server.base.controller;

import com.server.base.components.exceptions.CommonException;
import com.server.base.repository.dto.reference.AccountDto;
import com.server.base.repository.dto.request.SignInRequest;
import com.server.base.repository.dto.request.SignUpRequest;
import com.server.base.service.UserService;
import com.server.base.components.validations.AccountValid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created on 2023-05-12
 * Project user-service
 */
@RestController
@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;



    @GetMapping(value = "/sign/in")
    public ResponseEntity<AccountDto> signIn(@ModelAttribute @Valid @Validated(value = {AccountValid.SignIn.class})
                                             SignInRequest signInRequest, HttpServletResponse response) throws CommonException {
        return new ResponseEntity<>(service.signIn(signInRequest, response), HttpStatus.OK);
    }

    @PostMapping(value = "/sign/up")
    public ResponseEntity<AccountDto> signUp(@RequestBody @Valid @Validated(value = {AccountValid.SignUp.class})
                                             SignUpRequest signUpRequest,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws CommonException {
        return new ResponseEntity<>(service.signUp(signUpRequest, response), HttpStatus.OK);
    }

}
