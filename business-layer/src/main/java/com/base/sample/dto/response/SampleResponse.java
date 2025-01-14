package com.base.sample.dto.response;

import com.base.sample.dto.SampleQueryResult;

public record SampleResponse(
    Long sampleId,
    String sampleName
) {

    public static SampleResponse from( SampleQueryResult queryResult) {
        return new SampleResponse(queryResult.sampleId(), queryResult.sampleName());
    }
}
