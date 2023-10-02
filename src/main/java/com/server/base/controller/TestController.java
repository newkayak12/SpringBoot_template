package com.server.base.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping
    public String test(Principal principal) {
        return principal.getName();
    }
}
