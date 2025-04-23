package com.base.sample.usecase;

import com.base.config.UseCase;
import com.base.sample.ports.in.FindAllSampleQuery;
import com.base.sample.ports.out.FindAll;
import com.base.sample.ports.out.FindAll.SampleQueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class FindAllSampleUseCase implements FindAllSampleQuery {

    private final FindAll findAll;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public Page<FindAllResult> findAll(Pageable pageable) {
        return findAll.fetch(pageable).map(SampleQueryResult::toUseCaseResult);
    }
}
