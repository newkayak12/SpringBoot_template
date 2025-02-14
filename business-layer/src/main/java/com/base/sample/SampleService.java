package com.base.sample;

import com.base.sample.dto.ResponseConverter;
import com.base.sample.dto.SampleQueryResult;
import com.base.sample.dto.in.InsertSample;
import com.base.sample.dto.in.UpdateSample;
import com.base.sample.dto.out.SampleDto;
import com.base.sample.entity.Sample;
import com.base.sample.repository.SampleRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository repository;
    private final ResponseConverter converter = new ResponseConverter();

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<SampleDto> findAll() {
        List<SampleQueryResult> samples = repository.findAll();
        return samples.stream().map(converter::toSampleResponse).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Boolean insert(InsertSample request) {
        Sample entity = repository.save(request.toEntity());
        return Objects.nonNull(entity.getSampleId());
    }

    @Transactional
    public Boolean update(UpdateSample request) {

        try {
            Sample entity = repository.findById(request.id()).orElseThrow(IllegalArgumentException::new);
            entity.changeSampleName(request.sampleName());
        }
        catch (DataAccessException e) {
            return Boolean.FALSE;
        }


        return Boolean.TRUE;
    }
}
