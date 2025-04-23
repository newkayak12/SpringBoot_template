package com.base.sample.ports.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllSampleQuery {

    Page<FindAllResult> findAll(Pageable pageable);

    record FindAllResult(
        String id,
        String address,
        String detail,
        String name,
        int age
    ) {

    }
}
