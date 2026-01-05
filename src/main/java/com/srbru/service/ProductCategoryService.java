package com.srbru.service;

import java.util.List;

import com.srbru.binding.ProductCat;
import com.srbru.entity.ProductCategory;

public interface ProductCategoryService
{

	public boolean saveProductCategory(ProductCat category);

	public List<ProductCategory> getAllCategory();

	// public ProductCategory getProductCategoryById(Integer categoryid);

	public boolean updateProduct(ProductCat category);
//
//    public boolean statusChange(Integer categoryid, String activeSw);
}
