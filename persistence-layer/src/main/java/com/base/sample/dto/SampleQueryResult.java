package com.base.sample.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;

/**
 * DTO for {@link com.base.sample.entity.Sample}
 */
public record SampleQueryResult(
    Long sampleId,
    String sampleName
) implements Serializable {

    @QueryProjection
    public SampleQueryResult {
    }
}
