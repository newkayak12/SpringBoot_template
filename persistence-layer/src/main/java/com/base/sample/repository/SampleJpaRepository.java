package com.base.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.base.sample.entity.Sample;

public interface SampleJpaRepository extends JpaRepository<Sample, Long> {

    @Override
    Sample save(Sample entity);
}
