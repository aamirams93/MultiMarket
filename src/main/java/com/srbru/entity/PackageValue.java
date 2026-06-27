package com.srbru.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    @Column(name = "PID")
    private Integer pid;

    @Column(name = "PACK_LEVEL")
    private String packLevel;

    @Column(name = "PACK_AMOUNT")
    private BigDecimal packAmount;

    @Column(name = "PACK_STATUS")
    private String packStatus;

    @Column (name = "PACK_DATE", insertable = false)
    private Date packDate;
    
	@Column(name = "DAILY_AMOUNT_UPDATE", insertable = true)
    private LocalDate dailyAmountUpdate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", referencedColumnName = "USER_NO", nullable = false)
    private UserEntity customer;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "VIEW_IMAGE", columnDefinition = "bytea")
    private byte[] viewImage;
    

	@Column(name = "ACTION_DATE", insertable = false)
	private Date actionDate;
	
	@Column(name = "ACTION_BY")
	private String actionBy;

	@Column(name = "CUSTOMER_IP_ADDRESS")
	private String ipAddress;
	
	@Column(name = "Reject Remarks")
	private String rejectRemarks;
	
	

}

