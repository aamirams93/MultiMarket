package com.srbru.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "TBL_PACKAGE_LEVEL")
public class PackageLevel
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer lid;
	
	private String packageName;
	
	private Double packageDailyAmount;
}
