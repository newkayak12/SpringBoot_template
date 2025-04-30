package com.base.persistence.sample;

import static com.base.sample.entity.QSample.sample;

import com.base.sample.ports.out.FindAll;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindAllQueryDslRepository implements FindAll {

    private final JPAQueryFactory query;

    @Override
    public Page<SampleQueryResult> fetch(Pageable pageable) {
        return PageableExecutionUtils.getPage(
            list(pageable),
            pageable,
            count()::fetchOne
        );
    }

    private JPAQuery<Long> count() {
        return query.select(sample.id.count()).from(sample);
    }

    private List<SampleQueryResult> list(Pageable pageable) {
        JPAQuery<SampleQueryResult> select = query.select(
            Projections.constructor(
                SampleQueryResult.class,
                sample.id,
                sample.basicAddress.address,
                sample.basicAddress.detail,
                sample.personalInformation.name,
                sample.personalInformation.age
            )
        );

        if (pageable.isPaged()) {
            select.limit(pageable.getPageSize()).offset(pageable.getOffset());
        }

        return select.fetch();
    }
}
