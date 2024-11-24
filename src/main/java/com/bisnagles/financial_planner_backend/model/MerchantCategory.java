package com.bisnagles.financial_planner_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
public class MerchantCategory extends Auditable{
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Category category;

    @Override
    protected Long resolveOwnerId() {
        return (merchant != null && merchant.getOwnerId() != null)
                ? merchant.getOwnerId()
                : (category != null ? category.getOwnerId() : -1L);
    }

}
