package com.srbru.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srbru.binding.LoginUserRequest;
import com.srbru.binding.UserData;
import com.srbru.config.UserSessionService;
import com.srbru.security.service.JwtService;
import com.srbru.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerRestController 
{

	private final AuthenticationManager authManager;

	private final JwtService jwt;

	private final UserService userService;

	private final UserSessionService session;

	@GetMapping("/welcome")
	public String welcome()
	{
		return "welcome to Man Made";
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginUserRequest c,HttpServletRequest request,HttpServletResponse response)
	{
		try
		{
			Authentication auth = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(c.getEmailId(), c.getPassword()));

			if (auth.isAuthenticated())
			{

				// Generate JWT
				String accesToken = jwt.generateAccesToken(c.getEmailId());
	            String refreshToken = jwt.generateRefreshToken(c.getEmailId());
	            
	            ResponseCookie refrehCokkie = ResponseCookie.from("refreshToken",refreshToken)
	            		.httpOnly(true)
	            		.secure(true)
	            		.path("/api/v1/auth/refresh")
	            		.maxAge(7 * 24 * 60 * 60)
	            		.sameSite("Strict")
	            		.build();
	            response.addHeader(HttpHeaders.SET_COOKIE, refrehCokkie.toString());
				// Log login info correctly
				String clientIp = session.getClientIp(request);
				userService.logLoginSuccess(c.getEmailId(),clientIp);

				return new ResponseEntity<>(accesToken, HttpStatus.OK);
			}

		} catch (BadCredentialsException ex)
		{
			userService.isLoginBlocked(c.getEmailId());
			return new ResponseEntity<> ("Invalid credentials",HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>("Login failed", HttpStatus.BAD_REQUEST);
	}



	@PostMapping("/add")
	public ResponseEntity<String> addUser(@RequestBody UserData user,HttpServletRequest request)
	{
		    String clientIp = session.getClientIp(request);
			 userService.saveUser(user,clientIp);
		return new ResponseEntity<>("Account Created Successfully",HttpStatus.ACCEPTED);

	}

//	@PatchMapping("/activate")
//	public ResponseEntity<String> activateAccount(@RequestBody ActivateAccount activate)
//	{
//		boolean status = userService.activateUser(activate);
//
//		if (status)
//		{
//			return new ResponseEntity<>("Account Activated", HttpStatus.CREATED);
//		}
//		return new ResponseEntity<>("Inavlid Password", HttpStatus.BAD_REQUEST);
//
//	}

	@GetMapping("/alluser")
	public ResponseEntity<List<UserData>> gettAllUser()
	{
		List<UserData> allUser = userService.getAllUsers();

		return new ResponseEntity<>(allUser, HttpStatus.OK);
	}

	
	@PutMapping("/motp")
	public ResponseEntity<Void> otpEmailGen(@RequestBody UserData email) {
	    userService.generateEmailOtp(email);
	    return ResponseEntity.ok().header("X-Success-Message", "OTP sent successfully").build();
	}
	
	
	@PostMapping("/refresh")
	public ResponseEntity<Object> refresh(@CookieValue("refreshToken") String refreshToken) {
	    String email = jwt.extractUsername(refreshToken);
	    jwt.generateAccesToken(email);

	    return new ResponseEntity<Object>("", HttpStatus.OK);
	}
	




}
