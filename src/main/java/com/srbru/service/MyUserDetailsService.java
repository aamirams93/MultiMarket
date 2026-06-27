package com.srbru.service;



import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.srbru.entity.Authorities;
import com.srbru.entity.UserEntity;
import com.srbru.entity.UserPrincipal;
import com.srbru.repo.UserRepo;
import com.srbru.repo.AuthoritiesRepo;



import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MyUserDetailsService implements UserDetailsService
{

	private final UserRepo repo;
	
	private final AuthoritiesRepo authRepo;

	@Transactional
	@Override	
	public UserDetails loadUserByUsername(String emailId) {

	    UserEntity user = repo.findByEmailId(emailId)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    List<Authorities> roles = authRepo.findAuthorityByEmailId(emailId);

	    return new UserPrincipal(user, roles);
	}

}