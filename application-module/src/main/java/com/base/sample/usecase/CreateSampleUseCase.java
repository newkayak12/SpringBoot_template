package com.base.sample.usecase;

import com.base.config.UseCase;
import com.base.core.model.Address;
import com.base.sample.entity.Sample;
import com.base.sample.model.PersonalInformation;
import com.base.sample.ports.in.CreateSampleCommand;
import com.base.sample.ports.out.SaveSample;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class CreateSampleUseCase implements CreateSampleCommand {

    private final SaveSample saveSample;

    @Override
    @Transactional
    public boolean createSample(CreateSampleCommandRequest request) {
        Sample insertTarget = new Sample(
            new Address(request.address(), request.addressDetail()),
            new PersonalInformation(request.name(), request.age())
        );

        saveSample.save(insertTarget);

        return Boolean.TRUE;
    }
}
