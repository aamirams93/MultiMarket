package com.srbru.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.srbru.entity.ProductItems;

@RepositoryRestResource(exported = false)
public interface ProductRepo extends JpaRepository<ProductItems, Integer>
{

}
