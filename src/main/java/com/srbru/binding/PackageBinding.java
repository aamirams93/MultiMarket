package com.srbru.binding;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class PackageBinding
{

		private String emailId;

	    private  String packLevel;

	    private  BigDecimal packAmount;

	    private  MultipartFile viewImage;
		


}
