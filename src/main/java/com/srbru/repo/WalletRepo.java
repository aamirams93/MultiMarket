package com.srbru.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.srbru.entity.Walltet;


@Repository
public interface WalletRepo extends JpaRepository<Walltet, Integer>
{
	 Optional<Walltet> findByPid(Integer pid);
}
