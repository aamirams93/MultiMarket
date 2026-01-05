package com.srbru.service;

import java.util.List;
import java.util.Map;

import com.srbru.entity.ProductItems;

public interface ProductService
{
	public Map<Integer, String> getProductCategory();

	public boolean savePlan(ProductItems plan);

	public List<ProductItems> getAllProduct();

	public ProductItems getProductById(Integer planId);

	public boolean updateProduct(ProductItems plan);

	public boolean statusChange(Integer planId, String status);

}
