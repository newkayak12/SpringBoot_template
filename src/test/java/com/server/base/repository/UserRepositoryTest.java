package com.server.base.repository;

import com.server.base.components.exceptions.BecauseOf;
import com.server.base.repository.dto.request.SignInRequest;
import com.server.base.repository.userRepository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
@DataJpaTest(bootstrapMode = BootstrapMode.DEFAULT, showSql = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = {Configuration.class, Repository.class}
        )
})
@Profile("local")
public class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("로그인 테스트")
    public void signInTest(){
        //given
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUserId("test");
        signInRequest.setUserPwd("pwd");


        assertThatThrownBy(() -> {
            repository.signIn(signInRequest);
        })
        .hasMessage(BecauseOf.ACCOUNT_NOT_EXIST.getMsg());

    }
}
