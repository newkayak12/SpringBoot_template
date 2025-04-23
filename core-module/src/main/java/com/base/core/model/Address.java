package com.base.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Embeddable
public class Address {

    @Column(name = "address")
    private final String address;
    @Column(name = "detail")
    private final String detail;

    protected Address() {
        this(null, null);
    }
    public Address(String address, String detail) {
        this.address = address;
        this.detail = detail;
    }


}
