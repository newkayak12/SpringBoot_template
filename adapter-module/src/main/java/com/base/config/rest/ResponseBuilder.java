package com.base.config.rest;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface ResponseBuilder {


    static ResponseEntity<StringContainer> ok(final String body) {
        return ResponseEntity.ok(new StringContainer(body));
    }

    static ResponseEntity<BooleanContainer> ok(final Boolean body) {
        return ResponseEntity.ok(new BooleanContainer(body));
    }

    static <E> ResponseEntity<E> ok(final E body) {
        return ResponseEntity.ok(body);
    }

    static <E> ResponseEntity<PageContainer<E>> ok(Page<E> body) {
        return ResponseEntity.ok(PageContainer.of(body));
    }


    record BooleanContainer(
        boolean result
    ) {

    }

    record StringContainer(
        String message
    ) {

    }

    record PageContainer<E>(
        List<E> content,
        long totalElements,
        int totalPages,
        boolean hasContent,
        boolean hasNext,
        boolean hasPrevious
    ) {

        static <E> PageContainer<E> of(Page<E> page) {
            return new PageContainer<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasContent(),
                page.hasNext(),
                page.hasPrevious()
            );
        }
    }
}
