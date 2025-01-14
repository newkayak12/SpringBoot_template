package com.base.sample.repository;

import com.base.sample.dto.SampleQueryResult;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.base.sample.entity.Sample;

@Component
@RequiredArgsConstructor
public class SampleRepositoryAdapter  implements SampleRepository {
    private final SampleJpaRepository jpaRepository;
    private final SampleQueryDSLRepository queryDSLRepository;

    @Override
    public Sample save(Sample sample) {
        return jpaRepository.save(sample);
    }

    @Override
    public List<SampleQueryResult> findAll() {
        return queryDSLRepository.findAll();
    }

    @Override
    public Optional<Sample> findById(Long id) {
        return jpaRepository.findBySampleId(id);
    }
}
