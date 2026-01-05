package com.srbru.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Walltet
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer wid;
	
	private Double totalAmount;
	
	private Double dailyAmount;
	
	private Integer pid;
	
}
