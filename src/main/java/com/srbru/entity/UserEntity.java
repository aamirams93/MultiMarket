package com.srbru.entity;

import java.time.Instant;
import java.time.ZonedDateTime;


import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "USER")
public class UserEntity
{
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_no_seq")
    @SequenceGenerator(name = "user_no_seq", sequenceName = "user_no_sequence", allocationSize = 1)
    @Column(name = "USER_NO", nullable = false, unique = true)
    private Long userNo; // 6-digit sequential user number

	@Column(name = "CUSTOMER_NAME", nullable = false)
	private String fullName;

	@NotNull
	@Email
	@Size(min = 8)
	@Column(name = "CUSTOMER_EMAIL_ID")
	private String emailId;

	@Column(name = "CUSTOMER_MOBILE_NO", nullable = false)
	private Long mobileNo;

	@Column(name = "GENDER", nullable = false)
	private String gender;

	@Column(name = "ACCOUNT_STATUS")
	private boolean accStatus;

	@Column(name = "CUSTOMER_PASSWORD")
	private String password;

	@Column(name = "OTP_CREATION_TIME")
	@TimeZoneStorage(TimeZoneStorageType.COLUMN)
	private Instant otpCreationTime;

	@Column(name = "CREATED_DATE", updatable = false)
	@TimeZoneStorage(TimeZoneStorageType.COLUMN)
	private  ZonedDateTime createdDate;

	@Column(name = "UPDATED_DATE", insertable = false)
	@TimeZoneStorage(TimeZoneStorageType.COLUMN)
	private ZonedDateTime updatedDate;

	@Column(name = "CUSTOMER_IP_ADDRESS")
	private String ipAddress;

	

}