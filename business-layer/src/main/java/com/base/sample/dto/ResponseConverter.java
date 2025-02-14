package com.base.sample.dto;

import com.base.sample.dto.out.SampleDto;

public class ResponseConverter {

    public SampleDto toSampleResponse(SampleQueryResult sampleQueryResult) {
        return SampleDto.from(sampleQueryResult);
    }
}
