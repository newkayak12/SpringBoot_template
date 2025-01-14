package com.base.sample.dto.request;

import com.base.sample.entity.Sample;

public record InsertRequest(
    String sampleName
) {

    public Sample toEntity() {
        return Sample.of(sampleName);
    }
}
