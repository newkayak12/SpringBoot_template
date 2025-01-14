package com.base.sample.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.base.sample.entity.Sample;
import org.springframework.data.repository.Repository;

interface SampleJpaRepository extends Repository<Sample, Long> {

    Sample save(Sample entity);

    Optional<Sample> findBySampleId(Long id);
}
