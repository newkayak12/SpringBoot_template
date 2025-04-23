package com.base.sample.entity;

import com.base.config.TimeBasedUuidStrategy;
import com.base.core.model.Address;
import com.base.sample.model.PersonalInformation;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sample")
@Entity
public class Sample {

    @Id
    @TimeBasedUuidStrategy
    private String id;

    @AttributeOverrides({
        @AttributeOverride(column = @Column(name = "basic_address"), name = "address"),
        @AttributeOverride(column = @Column(name = "basic_address_detail"), name = "detail"),
    })
    @Embedded
    private Address basicAddress;
    @Embedded
    private PersonalInformation personalInformation;

    public Sample(Address basicAddress, PersonalInformation personalInformation) {
        this.basicAddress = basicAddress;
        this.personalInformation = personalInformation;
    }

    public void changeAddress(Address address) {
        basicAddress = address;
    }

    public void changeInformation(PersonalInformation information) {
        personalInformation = information;
    }
}

