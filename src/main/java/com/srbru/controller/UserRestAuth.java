package com.srbru.controller;



import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;

import com.srbru.binding.PackageBinding;
import com.srbru.binding.UserData;
import com.srbru.entity.UserEntity;
import com.srbru.repo.UserRepo;
import com.srbru.service.PackageValueService;
import com.srbru.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class UserRestAuth
{
	private final UserRepo repo;
	private final UserService uservice;
	private final PackageValueService pcService;

	/// abnomal things
	@GetMapping("/api/v1/me")
	public UserData getCurrentUser(@AuthenticationPrincipal UserDetails user)
	{
		String username = user.getUsername();  

		UserEntity userEntity = repo.findByEmailId(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		UserData userData = new UserData();

		BeanUtils.copyProperties(userEntity, userData);

		return userData;
	}
	
	
	@PostMapping("/api/v1/logout")
	public ResponseEntity<String> logout(HttpServletRequest request,
	                                     HttpServletResponse response) {

	    // 1Extract access token
	    String authHeader = request.getHeader("Authorization");
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.badRequest().body("No access token found");
	    }

	    String accessToken = authHeader.substring(7);

	    // CALL YOUR EXISTING METHOD
	    uservice.logoutUser(accessToken);

	    // revoke refresh token
	    ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
	            .httpOnly(true)
	            .secure(false)
	            //.sameSite("None")
	            .path("/")
	            .maxAge(0)
	            .build();

	    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

	    SecurityContextHolder.clearContext();

	    return ResponseEntity.ok("Logged out successfully");
	}

	@PostMapping(value="/api/v1/purchase",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String updatePackage(@AuthenticationPrincipal UserDetails user, @ModelAttribute PackageBinding binding)
	{
		binding.setEmailId(user.getUsername());
		 pcService.fetch(binding);
		 return "Package purchased successfully for user: " + user.getUsername();
	}


}
