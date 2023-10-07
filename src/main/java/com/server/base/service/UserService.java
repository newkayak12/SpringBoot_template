package com.server.base.service;

import com.server.base.components.base.BaseService;
import com.server.base.components.configure.security.jwt.TokenProvider;
import com.server.base.components.constants.Constants;
import com.server.base.components.exceptions.BecauseOf;
import com.server.base.components.exceptions.CommonException;
import com.server.base.repository.domains.Account;
import com.server.base.repository.dto.reference.AccountDto;
import com.server.base.repository.dto.request.SignInRequest;
import com.server.base.repository.dto.request.SignUpRequest;
import com.server.base.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserService extends BaseService implements UserDetailsService{
    private final UserRepository repository;
    private final TokenProvider tokenProvider;



    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUserId(userName);
        return repository.signIn(signInRequest).orElseThrow(() -> new UsernameNotFoundException(userName));
    }
    public AccountDto refreshAccessToken(SignInRequest signInRequest, HttpServletResponse response) throws CommonException {
        AccountDto dto = repository.signIn(signInRequest).orElseThrow(() -> new CommonException(BecauseOf.ACCOUNT_NOT_EXIST));

        Authentication authentication = new UsernamePasswordAuthenticationToken(dto, null);
        String token = tokenProvider.createToken(authentication);

        response.addHeader(Constants.AUTHORIZATION, token);
        return dto;
    }
    public AccountDto signIn(SignInRequest signInRequest, HttpServletResponse response) throws CommonException {
        AccountDto dto = repository.signIn(signInRequest).orElseThrow(() -> new CommonException(BecauseOf.ACCOUNT_NOT_EXIST));

        if( !this.isPasswordMatched(signInRequest.getUserPwd(), dto.getUserPwd()) ) throw new CommonException(BecauseOf.PASSWORD_NOT_MATCHED);
        Authentication authentication = new UsernamePasswordAuthenticationToken(dto, null);
        String token = tokenProvider.createToken(authentication);

        response.addHeader(Constants.AUTHORIZATION, token);
        response.addHeader(Constants.REFRESH_TOKEN, dto.getRefreshToken());

        return dto;
    }
    public AccountDto signUp(SignUpRequest signUpRequest, HttpServletResponse response) throws CommonException {
        if(Objects.nonNull(signUpRequest.getUserNo())) throw new CommonException(BecauseOf.ALREADY_EXIST_ACCOUNT);


        AccountDto dto =  signUpRequest;
        encryptPassword(dto);

        Authentication authentication = new UsernamePasswordAuthenticationToken(dto, null);
        String accessToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        dto.setRefreshToken(refreshToken);
        Account account = repository.save(mapper.map(dto, Account.class));
        dto = mapper.map(account, AccountDto.class);

        response.addHeader(Constants.AUTHORIZATION, accessToken);
        response.addHeader(Constants.REFRESH_TOKEN, refreshToken);
        return dto;
    }



}
