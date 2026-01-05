package com.srbru.controller;

import java.util.List;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srbru.binding.ApiResponse;
import com.srbru.constants.AppConstant;
import com.srbru.entity.ProductItems;
import com.srbru.service.MessageService;
import com.srbru.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductRestController extends BaseController
{
	private ProductService categoryService;

	private MessageService msgService;

	public ProductRestController(ProductService categoryService, MessageService msgService)
	{
		this.categoryService = categoryService;
		this.msgService = msgService;
	}

	@GetMapping("/plancategory")
	public ResponseEntity<Map<Integer, String>> planCategories()
	{
		Map<Integer, String> categories = categoryService.getProductCategory();

		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<String> addPlan(@RequestBody ProductItems product)
	{
		String responseMsg = AppConstant.EMPTY_STR;

		boolean isSaved = categoryService.savePlan(product);
		if (isSaved)
		{
			responseMsg = msgService.getMessage("", "");
		}

		return new ResponseEntity<>(responseMsg, HttpStatus.CREATED);
	}

	@PutMapping("/plan_update")
	public ResponseEntity<String> updatePlan(@RequestBody ProductItems product)
	{
		boolean updated = categoryService.savePlan(product);
		String msg = AppConstant.EMPTY_STR;
		if (updated)
		{
			msg = msgService.getMessage("", "");
		} else
		{
			msg = msgService.getMessage("", "");
		}
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	@GetMapping("/getplans")
	public ResponseEntity<List<ProductItems>> getPlans()
	{
		List<ProductItems> allproduct = categoryService.getAllProduct();
		return new ResponseEntity<List<ProductItems>>(allproduct, HttpStatus.OK);
	}

	@GetMapping("/plan_edit/{planId}")
	public ResponseEntity<ApiResponse<ProductItems>> editPlan(@PathVariable Integer productId)
	{
		try
		{
			String msg = AppConstant.EMPTY_STR;

			ProductItems planById = categoryService.getProductById(productId);

			if (planById != null)
			{
				msg = msgService.getMessage("wplan", "SAVE_SUCCESS");
				return buildResponse(msg, planById, true, HttpStatus.OK);
			} else
			{
				msg = msgService.getMessage("wplan", "SAVE_FAILED");
				return buildResponse(msg, null, false, HttpStatus.NOT_FOUND);
			}

		} catch (Exception ex)
		{
			String errorMsg = msgService.getMessage("wplan", "SAVE_SUCCESS");
			return buildResponse(errorMsg, null, false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping("/status-change/{productId}{status}")
	public ResponseEntity<String> changeStatus(@PathVariable Integer productId, @PathVariable String status)
	{
		boolean statusChange = categoryService.statusChange(productId, status);
		String msg = AppConstant.EMPTY_STR;
		if (statusChange)
		{
			msg = msgService.getMessage("", "");
		} else
		{
			msg = msgService.getMessage("", "");
		}
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

}
