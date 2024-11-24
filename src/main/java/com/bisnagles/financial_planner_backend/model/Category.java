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
public class Category extends Auditable{
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private Set<MerchantCategory> merchants;

    @Override
    protected Long resolveOwnerId() {
        // Use Java Streams to find the first non-null ownerId in categories
        return merchants != null
                ? merchants.stream()
                .map(MerchantCategory::getOwnerId)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(-1L)
                : -1L;
    }
}
