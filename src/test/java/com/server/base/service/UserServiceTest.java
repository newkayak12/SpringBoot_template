package com.server.base.service;

import com.server.base.components.constants.Constants;
import com.server.base.components.delegate.EmailDelegate;
import com.server.base.components.delegate.PasswordDelegate;
import com.server.base.components.exceptions.BecauseOf;
import com.server.base.components.exceptions.CommonException;
import com.server.base.repository.domains.Account;
import com.server.base.repository.dto.reference.AccountDto;
import com.server.base.repository.dto.request.SignInRequest;
import com.server.base.repository.dto.request.SignUpRequest;
import com.netflix_clone.userservice.repository.ticketRaiseRepository.TicketRaiseRepository;
import com.server.base.repository.userRepository.UserRepository;
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

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
public class UserServiceTest {
    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;
    @Mock
    private TicketRaiseRepository ticketRepository;
    @Spy
    private ModelMapper mapper;
    @Mock
    private HttpServletResponse response;
    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private TokenControl control;
    @Spy
    private PasswordDelegate passwordDelegate;
    @Mock
    private EmailDelegate emailDelegate;

    @BeforeEach
    public void setProperties() {
        Constants constants = new Constants();
        try {
            List<Method> methods = Arrays.stream(Constants.class.getMethods())
                    .filter(method -> Arrays.stream(method.getAnnotations())
                                            .anyMatch(annotation -> annotation.annotationType().equals(Value.class))
                    )
                    .collect(Collectors.toList());

            for ( Method method : methods ) {
                ReflectionUtils.invokeMethod(method, constants, "test");
            }
        } catch ( Exception e ){
            e.printStackTrace();
        }

    }

    @BeforeEach
    public void setModelMapperSet () {
        this.mapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
    }


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
            accountDto.setMobileNo("01012341234");
            accountDto.setEmail("test@test.com");
            accountDto.setIsSubscribed(false);
        }


        @Test
        @DisplayName(value = "성공")
        public void success () throws CommonException {

            //given
            SignInRequest request = new SignInRequest();
            request.setUserId("test");
            request.setUserPwd("1212");

            //when
            when(repository.signIn(request)).thenReturn(this.accountDto);
            when(ticketRepository.ticketInfoByUserNo(anyLong(), any())).thenReturn(null);
            when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
            when(control.encrypt(any(AccountDto.class))).thenReturn(anyString());

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
            when(repository.signIn(request)).thenReturn(null);

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
            when(repository.signIn(request)).thenReturn(this.accountDto);
            when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

            //then
            assertThatThrownBy(() -> service.signIn(request, response))
            .message().isEqualTo(BecauseOf.PASSWORD_NOT_MATCHED.getMsg());
        }
    }

    @Nested
    @DisplayName(value = "아이디 중복 확인 테스트")
    public class CheckDuplicatedIdTest {

        @Test
        @DisplayName(value = "성공")
        public void success() {
            //given
            //when
            when(repository.countAccountByUserId(anyString())).thenReturn(Optional.of(1L));
            //then
            assertThat(service.checkDuplicateId(anyString())).isEqualTo(false);
        }

        @Test
        @DisplayName(value = "실패")
        public void failure() {
            //given
            //when
            when(repository.countAccountByUserId(anyString())).thenReturn(Optional.of(0L));
            //then
            assertThat(service.checkDuplicateId(anyString())).isEqualTo(true);

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
            accountDto.setEmail(email);
            accountDto.setMobileNo(phone);
            accountDto.setUserNo(null);

            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setUserId(id);
            signUpRequest.setUserPwd(pwd);
            signUpRequest.setEmail(email);
            signUpRequest.setMobileNo(phone);

            AccountDto expect = new AccountDto();
            expect.setUserId(id);
            expect.setUserPwd(pwd);
            expect.setEmail(email);
            expect.setMobileNo(phone);
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

    @Nested
    @DisplayName(value = "비밀번호 변경 테스트")
    public class changePasswordTest {
        ChangePasswordRequest request;
        Account account;

        @BeforeEach
        public void setRequest() {
            this.request = new ChangePasswordRequest();
            request.setUserId("test");
            request.setUserPwd("1212");
            request.setNewUserPwd("1313");
        }

        @BeforeEach
        public void setAccount () {
            AccountDto dto = new AccountDto();
            dto.setUserId("test");
            this.account = mapper.map(dto, Account.class);
        }

        @Test
        @DisplayName(value = "성공")
        public void success () throws CommonException {
            //given

            //when
            doReturn(Optional.ofNullable(this.account)).when(repository).findAccountByUserId(anyString());
            when(bCryptPasswordEncoder.matches(anyString(), any())).thenReturn(true);
            doReturn(true).when(repository).changePassword(this.request);

            //then
            assertThat(service.changePassword(request)).isEqualTo(true);
        }

        @Test
        @DisplayName(value = "실패_계정 정보 없음")
        public void failure_no_data () throws CommonException {
            //given
//            when(bCryptPasswordEncoder.matches(anyString(), any())).thenReturn(true);
//            doReturn(true).when(repository).changePassword(this.request);
            //then
            assertThatThrownBy(() -> {
                service.changePassword(request);
            }).message().isEqualTo(BecauseOf.NO_DATA.getMsg());
        }

        @Test
        @DisplayName(value = "실패_패스워드 불일치")
        public void failure_pwd_not_matched() throws CommonException {
            //given
            doReturn(Optional.ofNullable(this.account)).when(repository).findAccountByUserId(anyString());
            when(bCryptPasswordEncoder.matches(anyString(), any())).thenReturn(false);
//            doReturn(true).when(repository).changePassword(this.request);
            //then
            assertThatThrownBy(() -> {
                service.changePassword(request);
            }).message().isEqualTo(BecauseOf.PASSWORD_NOT_MATCHED.getMsg());
        }

        @Test
        @DisplayName(value = "실패_업데이트 실패")
        public void failure_update_failure () throws CommonException {
            //given
            doReturn(Optional.ofNullable(this.account)).when(repository).findAccountByUserId(anyString());
            when(bCryptPasswordEncoder.matches(anyString(), any())).thenReturn(true);
            doReturn(false).when(repository).changePassword(this.request);
            //then
            assertThatThrownBy(() -> {
                service.changePassword(request);
            }).message().isEqualTo(BecauseOf.UPDATE_FAILURE.getMsg());
        }


    }

    @Nested
    @DisplayName(value = "아이디 찾기")
    public class FindIdTest {
        private FindAccountRequest findAccountRequest;
        private Account account;
        private String id = "test";
        private String phone = "01012341234";
        private String email = "test@test.com";

        @BeforeEach
        public void setFindAccountRequest() {
            this.findAccountRequest = new FindAccountRequest();
            this.findAccountRequest.setEmail(email);
            this.findAccountRequest.setMobileNo(phone);
            AccountDto dto = new AccountDto();
            dto.setUserId(this.id);
            this.account = mapper.map(dto, Account.class);
        }

        @Test
        @DisplayName(value = "성공")
        public void success() throws CommonException {
            //given
            //when
            doReturn(Optional.ofNullable(account))
            .when(repository)
            .findAccountByEmailAndMobileNo(email, phone);
            //then
            assertThat(service.findId(findAccountRequest)).isEqualTo(id);
        }
        @Test
        @DisplayName(value = "실패")
        public void failure() throws CommonException {
            //given
            //when
            doReturn(Optional.ofNullable(null))
                    .when(repository)
                    .findAccountByEmailAndMobileNo(email, phone);
            //then
            assertThatThrownBy(() -> service.findId(findAccountRequest))
                    .message()
                    .isEqualTo(BecauseOf.NO_DATA.getMsg());

        }
    }

    @Nested
    @DisplayName(value = "비밀번호 찾기")
    public class FindPasswordTest {
        private FindAccountRequest findAccountRequest;
        private String id = "test";
        private String phone = "01012341234";
        private String email = "test@test.com";

        @BeforeEach
        public void setFindAccountRequest() {
            this.findAccountRequest = new FindAccountRequest();
            this.findAccountRequest.setEmail(email);
            this.findAccountRequest.setMobileNo(phone);
            this.findAccountRequest.setUserId(id);
        }


        @Test
        @DisplayName(value = "success")
        public void success() throws CommonException {
            //given
            AccountDto accountDto = new AccountDto();
            accountDto.setUserNo(22L);
            accountDto.setUserId("test");
            accountDto.setUserPwd("$2a$10$7Ti7tDKkCZfDdbpHgVnQGuUZrbyGcHflYGtUlSiDVesI/jt.lIysS");
            accountDto.setRegDate(LocalDateTime.parse("2023-06-04T22:28:52"));
            accountDto.setIsAdult(false);
            accountDto.setMobileNo("01012341234");
            accountDto.setEmail("newkayak12@gmail.com");
            accountDto.setIsSubscribed(false);
            accountDto.setLastSignDate(LocalDateTime.parse("2023-06-05T20:26:41"));
            Account accountGiven = mapper.map(accountDto, Account.class);

            //when
            doReturn(Optional.of(accountGiven)).when(repository).findAccountByUserIdAndEmailAndMobileNo(id, email, phone);
            doReturn(new Account()).when(repository).save(any(Account.class));
            when(emailDelegate.sendPasswordReset(anyString(), anyString())).thenReturn(true);

            //then
            assertThat(service.findPassword(findAccountRequest))
                    .isEqualTo(true);
        }

        @Test
        @DisplayName(value = "failure")
        public void failure() throws CommonException {
            //given
            AccountDto accountDto = new AccountDto();
            accountDto.setUserNo(22L);
            accountDto.setUserId("test");
            accountDto.setUserPwd("$2a$10$7Ti7tDKkCZfDdbpHgVnQGuUZrbyGcHflYGtUlSiDVesI/jt.lIysS");
            accountDto.setRegDate(LocalDateTime.parse("2023-06-04T22:28:52"));
            accountDto.setIsAdult(false);
            accountDto.setMobileNo("01012341234");
            accountDto.setEmail("newkayak12@gmail.com");
            accountDto.setIsSubscribed(false);
            accountDto.setLastSignDate(LocalDateTime.parse("2023-06-05T20:26:41"));
            Account accountGiven = mapper.map(accountDto, Account.class);

            //when
            doReturn(Optional.of(accountGiven)).when(repository).findAccountByUserIdAndEmailAndMobileNo(id, email, phone);
            doReturn(new Account()).when(repository).save(any(Account.class));
            when(emailDelegate.sendPasswordReset(anyString(), anyString())).thenReturn(false);

            //then
            assertThatThrownBy(()->service.findPassword(findAccountRequest))
                    .message().isEqualTo(BecauseOf.UPDATE_FAILURE.getMsg());

        }
    }
}
