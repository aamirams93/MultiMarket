package com.srbru.service;



import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.srbru.entity.UserEntity;
import com.srbru.repo.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService
{

	private final UserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException
	{
		UserEntity user = repo.findByEmailId(emailId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + emailId));

		if (user == null)
		{
			throw new UsernameNotFoundException("User not found with email: " + emailId);
		}

		return User.withUsername(user.getEmailId()).password(user.getPassword()).roles("USER").build();
	}


}