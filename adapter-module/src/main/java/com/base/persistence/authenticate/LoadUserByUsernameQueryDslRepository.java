package com.base.persistence.authenticate;

import static com.base.authenticate.entity.QAuthenticate.authenticate;

import com.base.authenticate.port.out.LoadAuthenticate;
import com.base.config.security.jwt.AuthenticationDetails;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadUserByUsernameQueryDslRepository implements LoadAuthenticate {

    private final JPAQueryFactory query;

    @Override
    public Optional<AuthenticationDetails> load(String username) {
        return Optional.ofNullable(
            query.select(
                    Projections.constructor(
                        AuthenticationDetails.class,
                        authenticate.username,
                        authenticate.password,
                        authenticate.role
                    )
                )
                .from(authenticate)
                .where(authenticate.username.userId.eq(username))
                .fetchOne()
        );
    }
}
