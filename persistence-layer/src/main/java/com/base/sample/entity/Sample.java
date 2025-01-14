package com.base.sample.entity;

import com.base.AbstractTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "sample")
@Entity(name = "Sample")
public class Sample extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sampleId;
    private String sampleName;


    @Builder
    private Sample(Long sampleId, String sampleName) {
        this.sampleId = sampleId;
        this.sampleName = sampleName;
    }

    public static Sample of(String sampleName) {
        return new Sample(null, sampleName);
    }

    public Sample changeSampleName(String sampleName) {
        this.sampleName = sampleName;
        return this;
    }
}
