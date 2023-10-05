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
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tokenManager.TokenControl;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

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

    public AccountDto signIn(SignInRequest signInRequest, HttpServletResponse response) throws CommonException {
        AccountDto dto = repository.signIn(signInRequest).orElseThrow(() -> new CommonException(BecauseOf.ACCOUNT_NOT_EXIST));
        if(!this.isPasswordMatched(signInRequest.getUserPwd(), dto.getUserPwd())) throw new CommonException(BecauseOf.PASSWORD_NOT_MATCHED);

        Authentication authentication = new UsernamePasswordAuthenticationToken(dto, null);
        String token = tokenProvider.createToken(authentication);
        response.addHeader(Constants.AUTHORIZATION, token);

        return dto;
    }
    public AccountDto signUp(SignUpRequest signUpRequest, HttpServletResponse response) throws CommonException {
        if(Objects.nonNull(signUpRequest.getUserNo())) throw new CommonException(BecauseOf.ALREADY_EXIST_ACCOUNT);


        AccountDto dto =  signUpRequest;
        encryptPassword(dto);

        Account account = mapper.map(dto, Account.class);
        account = repository.save(account);
        dto = mapper.map(account, AccountDto.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(dto, null);
        String token = tokenProvider.createToken(authentication);
        response.addHeader(Constants.AUTHORIZATION, token);
        return dto;
    }


}
