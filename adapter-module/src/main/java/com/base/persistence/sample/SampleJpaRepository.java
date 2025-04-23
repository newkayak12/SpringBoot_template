package com.base.persistence.sample;

import com.base.sample.entity.Sample;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface SampleJpaRepository extends CrudRepository<Sample, String>{

    Sample save (Sample sample);

    Optional<Sample> findById(String id);
}
