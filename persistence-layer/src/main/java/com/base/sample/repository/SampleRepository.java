package com.base.sample.repository;

import com.base.sample.dto.SampleQueryResult;
import com.base.sample.entity.Sample;
import java.util.List;
import java.util.Optional;

public interface SampleRepository {
    Sample save(Sample sample);

    List<SampleQueryResult> findAll();

    Optional<Sample> findById(Long id);
}
