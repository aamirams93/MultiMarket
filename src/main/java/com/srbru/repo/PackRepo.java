package com.srbru.repo;


import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.srbru.entity.PackageValue;





@Repository
public interface PackRepo extends JpaRepository<PackageValue, Serializable>
{

	List<PackageValue> findPackagesByCustomerEmailId(@Param("emailId") String email);

    Page<PackageValue> findByPackStatus(String packStatus, Pageable pageable);
	
	Optional<PackageValue> findByPid(@Param("pid") Integer pid);
}
