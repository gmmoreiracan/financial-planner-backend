package com.bisnagles.financial_planner_backend.model;

import com.bisnagles.financial_planner_backend.repository.entity_listeners.CustomAuditingEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.ParamDef;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
@FilterDef(name = "itemOwnershipFilter", parameters = @ParamDef(name = "userId", type = Long.class))
@Filter(name = "itemOwnershipFilter", condition = "owner_id = :userId")
@EntityListeners({CustomAuditingEntityListener.class,AuditingEntityListener.class}) // Attach the listener here
public abstract class Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "bigint DEFAULT 0", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long version;

    @Column(name = "owner_id", updatable = false)
    private Long ownerId;

    // Getters and Setters
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by", insertable = false)
    private String lastModifiedBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;


    // Abstract method to be implemented by all subclasses
    protected abstract Long resolveOwnerId();

    // Method to set ownerId if it's not already set
    public void assignOwnerId() {
        if (this.ownerId == null) {
            this.ownerId = resolveOwnerId();
        }
    }
}
