package com.srbru.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.srbru.entity.PackageLevel;


@Repository
public interface PackLevelRepo extends JpaRepository<PackageLevel, Integer>
{

    Optional<PackageLevel> findByPackageNameIgnoreCase(String packageName);
}
