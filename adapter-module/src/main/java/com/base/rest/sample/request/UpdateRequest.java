package com.base.rest.sample.request;

import com.base.sample.ports.in.UpdateSampleCommand.UpdateSampleCommandRequest;

public record UpdateRequest(
    String address,
    String addressDetail,
    String name,
    int age
) {

    public UpdateSampleCommandRequest toUpdateCommand() {
        return new UpdateSampleCommandRequest(address, addressDetail, name, age);
    }
}
