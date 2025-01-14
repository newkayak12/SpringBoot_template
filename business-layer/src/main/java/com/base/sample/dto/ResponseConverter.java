package com.base.sample.dto;

import com.base.sample.dto.response.SampleResponse;

public class ResponseConverter {

    public SampleResponse toSampleResponse(SampleQueryResult sampleQueryResult) {
        return SampleResponse.from(sampleQueryResult);
    }
}
