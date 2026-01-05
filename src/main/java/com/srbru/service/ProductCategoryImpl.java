package com.srbru.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.srbru.binding.ProductCat;
import com.srbru.entity.ProductCategory;
import com.srbru.repo.ProductCategoryRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductCategoryImpl implements ProductCategoryService
{
	private final ProductCategoryRepo repo;

	@Override
	public boolean saveProductCategory(ProductCat category)
	{
		ProductCategory enity = new ProductCategory();
		BeanUtils.copyProperties(category, enity);
		ProductCategory saved = repo.save(enity);
		return saved.getCategoryid() != null;
	}

	@Override
	public List<ProductCategory> getAllCategory()
	{
		return repo.findAll();

	}
//
//    @Override
//    public ProductCategory getProductCategoryById(Integer categoryid)
//    {
//        Optional<ProductCategory> findById = repo.findById(categoryid);
//        if (findById.isPresent())
//        {
//            return findById.get();
//        } else
//        {
//            return null;
//        }
//    }

	@Override
	public boolean updateProduct(ProductCat category)
	{
		ProductCategory enity = new ProductCategory();
		BeanUtils.copyProperties(category, enity);
		ProductCategory saved = repo.save(enity);
		return saved.getCategoryid() != null;
	}
//
//    @Override
//    public boolean statusChange(Integer categoryid, String activeSw)
//    {
//
//        Optional<ProductCategory> findByid = repo.findById(categoryid);
//        if (findByid.isPresent())
//        {
//            ProductCategory product = findByid.get();
//            product.setActiveSw(activeSw);
//            repo.save(product);
//            return true;
//        }
//        return false;
//    }

}
