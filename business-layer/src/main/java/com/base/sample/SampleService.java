package com.base.sample;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SampleService {

    @Transactional(readOnly = true)
    public String sample() {
        return "";
    }
}
