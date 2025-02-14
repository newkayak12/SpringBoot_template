package com.base.sample.request;

import com.base.sample.dto.in.UpdateSample;

public record UpdateRequest(
    String sampleName
) {

    public UpdateSample toSample(Long id) {
        return new UpdateSample(id, sampleName());
    }
}
