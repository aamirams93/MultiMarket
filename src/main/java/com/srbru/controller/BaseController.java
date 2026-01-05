package com.srbru.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.srbru.binding.ApiResponse;

public abstract class BaseController
{

	protected <T> ResponseEntity<ApiResponse<T>> buildResponse(String message, T data, boolean success,
			HttpStatus status)
	{
		return new ResponseEntity<>(new ApiResponse<>(message, data, success), status);
	}
}
