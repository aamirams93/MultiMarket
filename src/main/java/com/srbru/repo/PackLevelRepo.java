package com.srbru.repo;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.srbru.entity.PackageLevel;


@Repository
public interface PackLevelRepo extends JpaRepository<PackageLevel, Serializable>
{

    Optional<PackageLevel> findByPackageNameIgnoreCase(String packageName);
    
}
