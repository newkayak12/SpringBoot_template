package com.base.sample.ports.out;

import com.base.sample.entity.Sample;
import java.util.Optional;

public interface LoadSample {

    Optional<Sample> findById(String id);
}
