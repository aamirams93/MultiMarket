//package com.srbru.entity;
//
//import java.time.LocalDate;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.Lob;
//import jakarta.persistence.ManyToOne;
//import lombok.Data;
//
//@Entity
//@Data
//public class PackageValue {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer pid;
//
//    private String packLevel;
//
//    private Double packAmount;
//
//    private char packStatus;
//
//    private LocalDate packDate;
//    
//    private LocalDate dailyAmountUpdate;
//
//    @ManyToOne(fetch = FetchType.LAZY)  
//    @JoinColumn(name = "customer_id", nullable = false)
//    private UserEntity customer;
//
//    @Lob
//    @Column(name = "VIEW_IMAGE", columnDefinition = "MEDIUMBLOB")
//    private byte[] viewImage;
//}
//
