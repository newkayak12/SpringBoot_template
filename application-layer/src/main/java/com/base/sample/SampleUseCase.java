package com.base.sample;

import com.base.component.UseCase;
import com.base.sample.request.InsertRequest;
import com.base.sample.request.UpdateRequest;
import com.base.sample.response.SampleResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class SampleUseCase {
    private final SampleService service;

    public List<SampleResponse> findAll() {
        return service.findAll().stream().map(SampleResponse::from).toList();
    }

    public Boolean insert(InsertRequest request) {
        return service.insert(request.toSample());
    }

    public Boolean update(Long id, UpdateRequest request) {
        return service.update(request.toSample(id));
    }
}
