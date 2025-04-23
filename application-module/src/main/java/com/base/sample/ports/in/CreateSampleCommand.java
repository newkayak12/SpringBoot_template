package com.base.sample.ports.in;

public interface CreateSampleCommand {

    boolean createSample(CreateSampleCommandRequest request);

    record CreateSampleCommandRequest(
        String address,
        String addressDetail,
        String name,
        int age
    ) {

    }
}
