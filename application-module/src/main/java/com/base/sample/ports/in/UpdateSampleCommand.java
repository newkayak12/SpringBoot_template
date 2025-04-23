package com.base.sample.ports.in;

public interface UpdateSampleCommand {

    boolean updateSample(String sampleId, UpdateSampleCommandRequest request);

    record UpdateSampleCommandRequest(
        String address,
        String addressDetail,
        String name,
        int age
    ) {


    }
}
