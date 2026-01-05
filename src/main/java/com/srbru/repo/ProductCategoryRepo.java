package com.srbru.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.srbru.entity.ProductCategory;

@RepositoryRestResource(exported = false)
public interface ProductCategoryRepo extends JpaRepository<ProductCategory, Integer>
{

}
