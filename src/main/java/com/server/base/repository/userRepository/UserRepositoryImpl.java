package com.server.base.repository.userRepository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.server.base.repository.domains.Account;
import com.querydsl.jpa.JPQLQueryFactory;
import com.server.base.repository.dto.reference.AccountDto;
import com.server.base.repository.dto.request.SignInRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Objects;
import java.util.Optional;

import static com.server.base.repository.domains.QAccount.account;
import com.server.base.repository.dto.reference.QAccountDto;
import org.springframework.util.StringUtils;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {
    private JPQLQueryFactory query;

    @Autowired
    public UserRepositoryImpl(JPQLQueryFactory query) {
        super(Account.class);
        this.query = query;
    }



    private BooleanExpression eqUserId(String userId){
        if(StringUtils.hasText(userId)) return account.userId.eq(userId);
        else return null;
    }
    private BooleanExpression eqUserNo(Long userNo) {
        if(Objects.nonNull(userNo)) return account.userNo.eq(userNo);
        else return null;
    }

    private BooleanExpression eqRefreshToken(String refreshToken) {
        if(StringUtils.hasText(refreshToken)) return account.refreshToken.eq(refreshToken);
        else return null;
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
                                        account.lastSignDate,
                                        account.role,
                                        account.refreshToken
                                )
                        )
                        .from(account)
                        .where(
                            this.eqUserId(signInRequest.getUserId()),
                            this.eqUserNo(signInRequest.getUserNo()),
                            this.eqRefreshToken(signInRequest.getRefreshToken())
                        )

                        .fetchOne()
        );

    }
}
