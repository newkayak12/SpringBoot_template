package com.server.base.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import com.server.base.components.configure.queryDsl.Config;
import com.server.base.components.exceptions.BecauseOf;
import com.server.base.components.exceptions.CommonException;
import com.server.base.repository.dto.reference.AccountDto;
import com.server.base.repository.dto.request.SignInRequest;
import com.server.base.repository.userRepository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.config.BootstrapMode;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@DataJpaTest(bootstrapMode = BootstrapMode.DEFAULT, showSql = true)
@Import(Config.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ComponentScan(includeFilters = {
//        @ComponentScan.Filter(
//                type = FilterType.ANNOTATION,
//                classes = {Configuration.class, Repository.class}
//        )
//})
@Profile("local")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;


    @Nested
    @DisplayName("로그인 테스트")
    public class SingInTest {
        @Test
        @DisplayName(value = "로그인 성공")
        public void signInSuccess(){
            //given
            SignInRequest signInRequest = new SignInRequest();
            signInRequest.setUserId("test");
            signInRequest.setUserPwd("pwd");

            assertThat(repository.signIn(signInRequest).orElseGet(() -> null))
                    .extracting(AccountDto::getUserId).isEqualTo("test");
        }

        @Test
        @DisplayName(value = "로그인 실패 성공")
        public void signInFailTestSuccess(){
            //given
            SignInRequest signInRequest = new SignInRequest();
            signInRequest.setUserId("test2");
            signInRequest.setUserPwd("pwd");

            assertThat(repository.signIn(signInRequest).orElseGet(() -> null))
                    .isEqualTo(null);
        }

        @Test
        @DisplayName(value = "로그인 실패")
        public void signInFailure(){
            //given
            SignInRequest signInRequest = new SignInRequest();
            signInRequest.setUserId("test23");
            signInRequest.setUserPwd("pwd");

            assertThatThrownBy(() -> repository.signIn(signInRequest).orElseThrow(() -> new CommonException(BecauseOf.ACCOUNT_NOT_EXIST)))
                    .hasMessage(BecauseOf.ACCOUNT_NOT_EXIST.getMsg());

        }

    }

}
