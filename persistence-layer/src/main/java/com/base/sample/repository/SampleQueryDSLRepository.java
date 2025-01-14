package com.base.sample.repository;

import static com.base.sample.entity.QSample.sample;

import com.base.sample.dto.QSampleQueryResult;
import com.base.sample.dto.SampleQueryResult;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class SampleQueryDSLRepository {
    private final JPAQueryFactory query;


    List<SampleQueryResult> findAll() {
        return query.select(
            new QSampleQueryResult(
                sample.sampleId,
                sample.sampleName
            )
        )
        .from(sample)
        .fetch();
    }
}
