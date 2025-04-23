package com.base.rest.sample.request;


import com.base.sample.ports.in.CreateSampleCommand.CreateSampleCommandRequest;

public record InsertRequest(
    String address,
    String addressDetail,
    String name,
    int age

) {

    public CreateSampleCommandRequest toInsertCommand() {
        return new CreateSampleCommandRequest(
            address,
            addressDetail,
            name,
            age
        );
    }
}
