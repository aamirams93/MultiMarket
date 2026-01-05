package com.srbru.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.srbru.entity.ProductCategory;
import com.srbru.entity.ProductItems;
import com.srbru.repo.ProductCategoryRepo;
import com.srbru.repo.ProductRepo;

@Service
public class PlroductServiceImpl implements ProductService
{

	private final ProductRepo prodcutRepo;

	private final ProductCategoryRepo productCategoryRepo;

	public PlroductServiceImpl(ProductRepo planRepo, ProductCategoryRepo plancCategoryRepo)
	{
		this.prodcutRepo = planRepo;
		this.productCategoryRepo = plancCategoryRepo;
	}

	@Override
	public Map<Integer, String> getProductCategory()
	{
		List<ProductCategory> categories = productCategoryRepo.findAll();
		Map<Integer, String> categoryMap = new HashMap<>();
		categories.forEach(category -> {
			categoryMap.put(category.getCategoryid(), category.getCategoryName());
		});
		return categoryMap;
	}

	public boolean savePlan(ProductItems product)
	{
		ProductItems saved = prodcutRepo.save(product);

		return saved.getProductId() != null;
	}

	@Override
	public List<ProductItems> getAllProduct()
	{
		return prodcutRepo.findAll();
	}

	@Override
	public ProductItems getProductById(Integer productId)
	{
		Optional<ProductItems> findById = prodcutRepo.findById(productId);
		if (findById.isPresent())
		{
			return findById.get();
		} else
		{
			return null;
		}

	}

	public boolean updateProduct(ProductItems product)
	{
		ProductItems saved = prodcutRepo.save(product);
		return saved.getProductId() != null;
	}

	@Override
	public boolean statusChange(Integer productId, String status)
	{
		Optional<ProductItems> findByid = prodcutRepo.findById(productId);
		if (findByid.isPresent())
		{
			ProductItems product = findByid.get();
			product.setActiveSw(status);
			prodcutRepo.save(product);
			return true;
		}
		return false;
	}

}
