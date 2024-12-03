package com.base.sample.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.base.sample.entity.Sample;

@Component
@RequiredArgsConstructor
public class SampleRepositoryAdapter implements SampleRepository{
    private final SampleJpaRepository jpaRepository;
    private final SampleQueryDSLRepository queryDSLRepository;

    @Override
    public Sample save(Sample sample) {
        return jpaRepository.save(sample);
    }
}
