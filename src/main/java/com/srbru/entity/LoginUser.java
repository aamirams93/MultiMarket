package com.srbru.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "LOGGED_USER")
@Data
public class LoginUser
{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "CUSTOMER_USER_ID")
	private String userNo;

	@Column(name = "FIRST_LOGIN_DATE_TIME", updatable = false)
	@TimeZoneStorage(TimeZoneStorageType.COLUMN)
	private ZonedDateTime createdDate;

	@Column(name = "LAST_LOGIN_DATE_TIME", insertable = false)
	@TimeZoneStorage(TimeZoneStorageType.COLUMN)
	private ZonedDateTime updatedDate;

	@Column(name = "CUSTOMER_IP_ADDRESS")
	private String ipAddress;
	
	@Column(name = "LOGGED_STATUS")
	private String loggedStatus;

	@Column(name = "LOCKED_STATUS")
	private String lockedStatus;
	
	@PrePersist
	protected void onCreate() {
	    this.createdDate = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
	}

}
