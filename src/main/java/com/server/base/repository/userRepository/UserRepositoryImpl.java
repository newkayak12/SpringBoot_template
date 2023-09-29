package com.server.base.repository.userRepository;

import com.server.base.repository.domains.Account;
import com.querydsl.jpa.JPQLQueryFactory;
import com.server.base.repository.dto.reference.AccountDto;
import com.server.base.repository.dto.request.SignInRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import java.util.Optional;

import static com.server.base.repository.domains.QAccount.account;
import com.server.base.repository.dto.reference.QAccountDto;
public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {
    private JPQLQueryFactory query;

    @Autowired
    public UserRepositoryImpl(JPQLQueryFactory query) {
        super(Account.class);
        this.query = query;
    }


    @Override
    public Optional<AccountDto> signIn(SignInRequest signInRequest) {
        return Optional.ofNullable(
                query.select(
                                new QAccountDto(
                                        account.userNo,
                                        account.userId,
                                        account.userPwd,
                                        account.regDate,
                                        account.lastSignDate
                                )
                        )
                        .from(account)
                        .where(account.userId.eq(signInRequest.getUserId()))
                        .fetchOne()
        );

    }
}
