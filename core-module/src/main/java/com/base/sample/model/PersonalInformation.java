package com.base.sample.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Embeddable
public class PersonalInformation {
    private final String name;
    private final int age;

    protected PersonalInformation() {
        this(null, 0);
    }

    public PersonalInformation(String name, int age) {
        this.name = name;
        this.age = age;
    }


}
