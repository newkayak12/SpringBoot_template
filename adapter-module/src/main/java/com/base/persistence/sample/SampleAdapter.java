package com.base.persistence.sample;

import com.base.sample.entity.Sample;
import com.base.sample.ports.out.LoadSample;
import com.base.sample.ports.out.SaveSample;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SampleAdapter implements SaveSample, LoadSample {
    private final SampleJpaRepository jpaRepository;

    @Override
    public Sample save(Sample sample) {
        return jpaRepository.save(sample);
    }

    @Override
    public Optional<Sample> findById(String id) {
        return jpaRepository.findById(id);
    }
}
