package com.base.rest.sample;

import com.base.config.rest.ResponseBuilder;
import com.base.config.rest.ResponseBuilder.BooleanContainer;
import com.base.config.rest.ResponseBuilder.PageContainer;
import com.base.config.url.sample.URL.SampleV1;
import com.base.rest.sample.request.InsertRequest;
import com.base.rest.sample.request.UpdateRequest;
import com.base.sample.ports.in.CreateSampleCommand;
import com.base.sample.ports.in.FindAllSampleQuery;
import com.base.sample.ports.in.FindAllSampleQuery.FindAllResult;
import com.base.sample.ports.in.UpdateSampleCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {

    private final CreateSampleCommand createCommand;
    private final UpdateSampleCommand updateCommand;
    private final FindAllSampleQuery findAllQuery;

    @GetMapping(value = SampleV1.FIND_ALL)
    public ResponseEntity<PageContainer<FindAllResult>> findAll(Pageable pageable) {
        return ResponseBuilder.ok(findAllQuery.findAll(pageable));
    }

    @PostMapping(value = SampleV1.CREATE)
    public ResponseEntity<BooleanContainer> insert(@RequestBody InsertRequest request) {
        return ResponseBuilder.ok(createCommand.createSample(request.toInsertCommand()));
    }

    @PatchMapping(value = SampleV1.UPDATE)
    public ResponseEntity<BooleanContainer> update(@PathVariable(name = "id") String id, UpdateRequest request) {
        return ResponseBuilder.ok(updateCommand.updateSample(id, request.toUpdateCommand()));
    }
}
