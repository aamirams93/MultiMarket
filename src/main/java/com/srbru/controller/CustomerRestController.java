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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srbru.binding.ActivateAccount;
import com.srbru.binding.ApiResponse;
import com.srbru.binding.LoginUserRequest;
import com.srbru.binding.UserData;
import com.srbru.config.UserSessionService;
import com.srbru.constants.AppConstant;
import com.srbru.repo.UserRepo;
import com.srbru.security.service.JwtService;
import com.srbru.service.MessageService;
import com.srbru.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerRestController extends BaseController
{

	private final AuthenticationManager authManager;

	private final JwtService jwt;

	private final UserService userService;

	private final MessageService msgService;

	private final UserRepo repo;

	private final UserSessionService session;

	@GetMapping("/welcome")
	public String welcome()
	{
		return "welcome to Man Made";
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Object>> login(@RequestBody LoginUserRequest c,HttpServletRequest request,HttpServletResponse response)
	{
		String email = c.getEmailId();

		if (userService.isLoginBlocked(email))
		{
			return buildResponse("User already logged in",null,false,HttpStatus.UNAUTHORIZED);
		}

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

				return buildResponse(accesToken, null, false, HttpStatus.OK);
			}

		} catch (BadCredentialsException ex)
		{
			userService.isLoginBlocked(c.getEmailId());
			return buildResponse("Invalid credentials", null, false, HttpStatus.UNAUTHORIZED);
		}

		return buildResponse("Login failed", null, false, HttpStatus.BAD_REQUEST);
	}



	@PostMapping("/add")
	public ResponseEntity<ApiResponse<Boolean>> addUser(@RequestBody UserData user)
	{
		String msg = AppConstant.EMPTY_STR;
		try
		{

			if (repo.existsByEmailId(user.getEmailId()))
			{

				return buildResponse(msg, null, false, HttpStatus.CONFLICT);
			}

			if (repo.existsByMobileNo(user.getMobileNo()))
			{
				return buildResponse(msg, null, false, HttpStatus.CONFLICT);
			}
			boolean saved = userService.saveUser(user);

			if (saved)
			{
				msg = msgService.getMessage("WUSER", "GETUSER");
				return buildResponse(msg, saved, true, HttpStatus.OK);
			}
			msg = msgService.getMessage("WUSER", "GETUSER");
			return buildResponse(msg, saved, true, HttpStatus.OK);
		} catch (Exception e)

		{
			msg = msgService.getMessage("WUSER", "NOTVAlID");
			return buildResponse(msg, null, false, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PatchMapping("/activate")
	public ResponseEntity<String> activateAccount(@RequestBody ActivateAccount activate)
	{
		boolean status = userService.activateUser(activate);

		if (status)
		{
			return new ResponseEntity<>("Account Activated", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Inavlid Password", HttpStatus.BAD_REQUEST);

	}

	@GetMapping("/alluser")
	public ResponseEntity<List<UserData>> gettAllUser()
	{
		List<UserData> allUser = userService.getAllUsers();

		return new ResponseEntity<>(allUser, HttpStatus.OK);
	}

	@PostMapping("change/{userId}/{accStatus}")
	public ResponseEntity<ApiResponse<Boolean>> changeStatus(@PathVariable Integer userId,
			@PathVariable boolean accStatus)
	{
		String msg = AppConstant.EMPTY_STR;
		try
		{
			boolean status = userService.changeStatus(userId, accStatus);
			if (status)
			{
				msg = msgService.getMessage("WUSER", "GETUSER");
				return buildResponse(msg, status, true, HttpStatus.OK);

			}
			msg = msgService.getMessage("WUSER", "GETUSERNOT");
			return buildResponse(msg, status, true, HttpStatus.NOT_FOUND);
		} catch (Exception e)
		{
			msg = msgService.getMessage("WUSER", "NOTVAlID");
			return buildResponse(msg, null, false, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PutMapping("/motp")
	public ResponseEntity<ApiResponse<Boolean>> otpEmailGen(@RequestBody UserData email)
	{
		if (email.getEmailId() == null || email.getEmailId().isBlank())
		{
			return buildResponse("Email is required", null, false, HttpStatus.BAD_REQUEST);
		}

		boolean result = userService.generateEmailOtp(email);

		if (result)
		{
			return buildResponse("OTP sent successfully to email", null, true, HttpStatus.OK);
		} else
		{
			return buildResponse("Email not registered", null, false, HttpStatus.NOT_FOUND);
		}
	}
	
	
	
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<Object>> refresh(
	        @CookieValue("refreshToken") String refreshToken) {

	    if (!jwt.validateRefreshToken(refreshToken) ||
	        !jwt.isRefreshToken(refreshToken)) {

	        return buildResponse(
	                "Invalid refresh token",
	                null,
	                false,
	                HttpStatus.FORBIDDEN
	        );
	    }

	    String email = jwt.extractUsername(refreshToken);
	    String newAccessToken = jwt.generateAccesToken(email);

	    return buildResponse(newAccessToken, null, false, HttpStatus.OK);
	}


}
