package com.srbru.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "PRODUCT_CATEGORY")
public class ProductCategory
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "Category_ID")
	private Integer categoryid;

	@Column(name = "Category_Name")
	private String categoryName;

	@Column(name = "Active_SW")
	private String activeSw;

	@Column(name = "Created_BY")
	private String createdBy;

	@Column(name = "Updated_BY")
	private String updatedBy;

	@Column(name = "Created_Date", updatable = false)
	@CreationTimestamp
	private LocalDateTime createdDate;

	@Column(name = "Updated_Date", insertable = false)
	@UpdateTimestamp
	private LocalDateTime updatedDate;

}
