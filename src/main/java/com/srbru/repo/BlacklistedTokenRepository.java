package com.srbru.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.srbru.entity.BlacklistedToken;

@Repository
@RepositoryRestResource(exported = false)
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, String>
{
	boolean existsByJti(String jti);
}
