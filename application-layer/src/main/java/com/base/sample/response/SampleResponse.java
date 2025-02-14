package com.base.sample.response;

import com.base.sample.dto.out.SampleDto;

public record SampleResponse(
    Long sampleId,
    String sampleName
) {

    public static SampleResponse from( SampleDto queryResult) {
        return new SampleResponse(queryResult.sampleId(), queryResult.sampleName());
    }
}
