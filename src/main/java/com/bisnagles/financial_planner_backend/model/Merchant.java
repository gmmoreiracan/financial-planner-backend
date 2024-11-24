package com.bisnagles.financial_planner_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
public class Merchant extends Auditable{
    private String name;

    @OneToMany(mappedBy = "merchant")
    @JsonIgnore
    private Set<MerchantCategory> categories;

    @Override
    protected Long resolveOwnerId() {
        // Use Java Streams to find the first non-null ownerId in categories
        return categories != null
                ? categories.stream()
                .map(MerchantCategory::getOwnerId)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(-1L)
                : -1L;
    }
}