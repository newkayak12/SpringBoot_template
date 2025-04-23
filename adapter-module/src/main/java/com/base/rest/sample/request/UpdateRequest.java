package com.base.rest.sample.request;

import com.base.sample.ports.in.update.UpdateSampleCommand.UpdateSampleCommandRequest;

public record UpdateRequest(
    String sampleName
) {

    public UpdateSampleCommandRequest toUpdateCommand() {
        return UpdateSampleCommandRequest.of(sampleName);
    }
}
