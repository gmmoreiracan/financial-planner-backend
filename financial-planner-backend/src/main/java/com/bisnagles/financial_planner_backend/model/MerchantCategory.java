package com.bisnagles.financial_planner_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
public class MerchantCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Category category;

}
