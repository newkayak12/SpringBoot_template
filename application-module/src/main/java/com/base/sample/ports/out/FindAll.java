package com.base.sample.ports.out;


import com.base.sample.ports.in.FindAllSampleQuery.FindAllResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@FunctionalInterface
public interface FindAll {

    Page<SampleQueryResult> fetch(Pageable pageable);


    record SampleQueryResult(
        String id,
        String address,
        String detail,
        String name,
        int age
    ) {

        public FindAllResult toUseCaseResult() {
            return new FindAllResult(
                id,
                address,
                detail,
                name,
                age
            );
        }

    }
}
