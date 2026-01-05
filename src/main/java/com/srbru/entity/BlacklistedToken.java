package com.srbru.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "BLACK_LISTED_TOKEN")
public class BlacklistedToken
{

	@Id
	@Column(name = "TOKEN",nullable = false,length = 100)
	private String token;

	@Column(name = "TOKEN_EXPIRY_DATE",nullable = false)
	private Date expiryDate;

	public BlacklistedToken()
	{
	}

    public BlacklistedToken(String token, Date expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }

}
