package com.base.sample.dto.in;

import com.base.sample.entity.Sample;

public record InsertSample(
    String sampleName
) {

    public Sample toEntity() {
        return Sample.of(sampleName);
    }
}
