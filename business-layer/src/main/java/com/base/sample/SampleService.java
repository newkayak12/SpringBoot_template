package com.base.sample;

import com.base.sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SampleService {
    private final SampleRepository repository;

    @Transactional(readOnly = true)
    public String sample() {
        return "";
    }
}
