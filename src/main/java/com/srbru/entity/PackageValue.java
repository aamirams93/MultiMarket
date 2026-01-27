package com.srbru.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "TBL_PACKAGE_VALUE")
public class PackageValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;

    private String packLevel;

    private Double packAmount;

    private String packStatus;

    private LocalDate packDate;
    
    private LocalDate dailyAmountUpdate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", referencedColumnName = "USER_NO", nullable = false)
    private UserEntity customer;

    @Lob
    @Column(name = "VIEW_IMAGE", columnDefinition = "bytea")
    private byte[] viewImage;
}

