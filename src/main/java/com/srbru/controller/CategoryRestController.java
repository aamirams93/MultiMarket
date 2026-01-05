package com.srbru.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srbru.binding.ProductCat;
import com.srbru.constants.AppConstant;
import com.srbru.entity.ProductCategory;
import com.srbru.service.MessageService;
import com.srbru.service.ProductCategoryService;

@RestController
@RequestMapping("/category")
public class CategoryRestController extends BaseController
{
	private MessageService msgService;
	private ProductCategoryService categoryService;

	public CategoryRestController(ProductCategoryService categoryService, MessageService msgService)
	{
		this.categoryService = categoryService;
		this.msgService = msgService;
	}

	@PostMapping("/add")
	public ResponseEntity<String> addCategory(@RequestBody ProductCat category)
	{
		String responseMsg = AppConstant.EMPTY_STR;

		boolean isSaved = categoryService.saveProductCategory(category);
		if (isSaved)
		{
			responseMsg = msgService.getMessage("", "");
		}

		return new ResponseEntity<>(responseMsg, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updatePlan(@RequestBody ProductCat category)
	{
		boolean updated = categoryService.saveProductCategory(category);
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

	@GetMapping("/getCategory")
	public ResponseEntity<List<ProductCategory>> getPlans()
	{
		List<ProductCategory> allproduct = categoryService.getAllCategory();
		return new ResponseEntity<List<ProductCategory>>(allproduct, HttpStatus.OK);
	}

}
