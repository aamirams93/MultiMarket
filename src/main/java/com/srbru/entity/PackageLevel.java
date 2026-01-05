package com.srbru.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PackageLevel
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer lid;
	
	private String packageName;
	
	private Double packageDailyAmount;
}
