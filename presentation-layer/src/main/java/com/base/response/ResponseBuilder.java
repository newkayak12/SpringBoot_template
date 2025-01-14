package com.base.response;

import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface ResponseBuilder<T> {

     static <E extends String> ResponseEntity<Map<String, String>> ok( E body ) {
        return ResponseEntity.ok(Map.of("message", body));
    }

     static <E extends Boolean> ResponseEntity<Map<String, Boolean>>  ok( E body ) {
        return ResponseEntity.ok(Map.of("result", body));
    }

    static <E> ResponseEntity<E> ok( E body ) {
        return ResponseEntity.ok(body);
    }
}
