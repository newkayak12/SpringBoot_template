package com.server.base.service;

import com.server.base.components.configure.security.jwt.TokenProvider;
import com.server.base.components.constants.Constants;
import com.server.base.components.exceptions.BecauseOf;
import com.server.base.components.exceptions.CommonException;
import com.server.base.repository.domains.Account;
import com.server.base.repository.dto.reference.AccountDto;
import com.server.base.repository.dto.request.SignInRequest;
import com.server.base.repository.dto.request.SignUpRequest;
import com.server.base.repository.userRepository.UserRepository;
import com.server.base.util.AbstractServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tokenManager.TokenControl;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UserServiceTest extends AbstractServiceTest {
    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
    @Mock
    private HttpServletResponse response;
    @Mock
    private TokenControl control;

    @Mock
    private TokenProvider tokenProvider;




    @Nested
    @DisplayName(value = "로그인 service 테스트")
    public class SignInTest {
        AccountDto accountDto = null;

        @BeforeEach
        public void setAccountDto() {
            accountDto = new AccountDto();
            accountDto.setUserNo(22L);
            accountDto.setUserId("test");
            accountDto.setUserPwd("$2a$10$7Ti7tDKkCZfDdbpHgVnQGuUZrbyGcHflYGtUlSiDVesI/jt.lIysS");
        }


        @Test
        @DisplayName(value = "성공")
        public void success () throws CommonException {

            //given
            SignInRequest request = new SignInRequest();
            request.setUserId("test");
            request.setUserPwd("1212");

            //when
            when(repository.signIn(request)).thenReturn(Optional.ofNullable(this.accountDto));
            when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
//            when(control.encrypt(any(AccountDto.class))).thenReturn(anyString());
            when(tokenProvider.createToken(any())).thenReturn(anyString());

            //then
            assertThat(service.signIn(request, response))
                    .extracting(AccountDto::getUserId).isEqualTo(this.accountDto.getUserId());

        }

        @Test
        @DisplayName(value = "실패 - 아이디 틀림")
        public void failure_Id () throws CommonException {

            //given
            SignInRequest request = new SignInRequest();
            request.setUserId("test");
            request.setUserPwd("1213");

            //when
            when(repository.signIn(request)).thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> service.signIn(request, response))
            .message().isEqualTo(BecauseOf.ACCOUNT_NOT_EXIST.getMsg());
        }

        @Test
        @DisplayName(value = "실패 - 비밀번호 틀림")
        public void failure_pwd () throws CommonException {

            //given
            SignInRequest request = new SignInRequest();
            request.setUserId("test");
            request.setUserPwd("1213");

            //when
            when(repository.signIn(request)).thenReturn(Optional.ofNullable(this.accountDto));
            when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);
//            when(tokenProvider.createToken(any())).thenReturn(anyString());

            //then
            assertThatThrownBy(() -> service.signIn(request, response))
            .message().isEqualTo(BecauseOf.PASSWORD_NOT_MATCHED.getMsg());
        }
    }


    @Nested
    @DisplayName(value = "로그인 테스트")
    public class SignUpTest {

        @Test
        @DisplayName(value = "성공")
        public void success () throws CommonException {
            //given
            String id = "test2";
            String email = "email@eamil.com";
            String pwd = "1313";
            String phone = "01012341234";
            AccountDto accountDto = new AccountDto();
            accountDto.setUserId(id);
            accountDto.setUserPwd(pwd);
            accountDto.setUserNo(null);

            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setUserId(id);
            signUpRequest.setUserPwd(pwd);

            AccountDto expect = new AccountDto();
            expect.setUserId(id);
            expect.setUserPwd(pwd);
            expect.setUserNo(2L);

            Account result = mapper.map(expect, Account.class);


            //when
            when(bCryptPasswordEncoder.encode(anyString())).thenReturn(null);
            doReturn(result).when(repository).save(any(Account.class));


            System.out.println(accountDto);
            //then
            assertThat(service.signUp(signUpRequest, response))
                    .isEqualTo(expect);
        }

    }

}
