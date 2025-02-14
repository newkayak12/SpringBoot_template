package com.base.sample.request;


import com.base.sample.dto.in.InsertSample;

public record InsertRequest(
    String sampleName
) {

    public  InsertSample toSample(){
        return new InsertSample(sampleName());
    }
}
