package com.base.controller;

import com.base.config.URL.Sample;
import com.base.response.ResponseBuilder;
import com.base.sample.SampleService;
import com.base.sample.dto.request.InsertRequest;
import com.base.sample.dto.request.UpdateRequest;
import lombok.RequiredArgsConstructor;
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
    private final SampleService service;

    @GetMapping(value = Sample.FIND_ALL)
    public ResponseEntity findAll() {
        return ResponseBuilder.ok(service.findAll());
    }

    @PostMapping(value = Sample.CREATE)
    public ResponseEntity insert(@RequestBody InsertRequest request) {
        return ResponseBuilder.ok(service.insert(request));
    }

    @PatchMapping(value = Sample.UPDATE)
    public ResponseEntity update( @PathVariable(name = "id") Long id, UpdateRequest request) {
        return ResponseBuilder.ok(service.update(id, request));
    }
}
