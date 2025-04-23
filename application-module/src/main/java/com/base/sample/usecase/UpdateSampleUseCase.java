package com.base.sample.usecase;

import com.base.config.UseCase;
import com.base.core.model.Address;
import com.base.sample.entity.Sample;
import com.base.sample.model.PersonalInformation;
import com.base.sample.ports.in.UpdateSampleCommand;
import com.base.sample.ports.out.LoadSample;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class UpdateSampleUseCase implements UpdateSampleCommand {

    private final LoadSample loadSample;

    @Override
    @Transactional
    public boolean updateSample(String sampleId, UpdateSampleCommandRequest request) {
        Sample sample = loadSample.findById(sampleId).orElseThrow(NoSuchElementException::new);

        sample.changeAddress(new Address(request.address(), request.addressDetail()));
        sample.changeInformation(new PersonalInformation(request.name(), request.age()));

        return Boolean.TRUE;
    }
}
