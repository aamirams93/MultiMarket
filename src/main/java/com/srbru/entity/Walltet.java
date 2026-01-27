package com.srbru.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "TBL_USERS_WALLET")
public class Walltet
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer wid;
	
	private Double totalAmount;
	
	private Double dailyAmount;
	
	private Integer pid;
	
}
