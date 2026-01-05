package com.srbru.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.srbru.binding.ActivateAccount;
import com.srbru.binding.UserData;
import com.srbru.config.UserSessionService;
import com.srbru.entity.Authorities;
import com.srbru.entity.LoginUser;
import com.srbru.entity.UserEntity;
import com.srbru.repo.AuthoritiesRepo;
import com.srbru.repo.LoginUserRepo;
import com.srbru.repo.UserRepo;
import com.srbru.security.service.JwtService;
import com.srbru.utils.EmailService;
import com.srbru.utils.LoginCredValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service

@AllArgsConstructor
public class UserManagementServiceImpl implements UserService
{

	private final UserRepo repo;

	private final EmailService emailutils;

	private final AuthoritiesRepo authRepo;

	private PasswordEncoder passwordEncoder;

	//private final UserSessionService session;

	private final LoginUserRepo loginRepo;

	private final JwtService jwt;
	


	    public String getClientIpFromFilter() {

	        ServletRequestAttributes attrs =
	                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

	        if (attrs == null) {
	            return "UNKNOWN";
	        }

	        HttpServletRequest request = attrs.getRequest();

	        String ip = request.getHeader("X-Forwarded-For");
	        if (ip != null && !ip.isBlank()) {
	            return ip.split(",")[0]; // first IP
	        }

	        return request.getRemoteAddr();
	    }
	

	

	@Override
	public boolean saveUser(UserData userData)
	{

		try
		{

			if (repo.existsByEmailId(userData.getEmailId()))
			{
				return false;
			}

			if (repo.existsByMobileNo(userData.getMobileNo()))
			{
				return false;
			}

			UserEntity entity = new UserEntity();
			BeanUtils.copyProperties(userData, entity);

			entity.setIpAddress(getClientIpFromFilter());
			entity.setAccStatus(true);
			entity.setCreatedDate(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")));

			UserEntity savedUser = repo.save(entity);

			// Save role
			Authorities auth = new Authorities();
			auth.setEmail(userData.getEmailId());
			auth.setAuthority("ROLE_USER");
			authRepo.save(auth);

			return savedUser.getUserNo() != null;

		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean generateEmailOtp(UserData email)
	{
		try
		{

			if (email.getEmailId() == null || email.getEmailId().isEmpty())			{
				return false;
			}
			Optional<UserEntity> mail = repo.findByEmailId(email.getEmailId());

			if (mail.isEmpty())
			{
				return false;
			}

			UserEntity entity = mail.get();

			// 🔹 Generate OTP
			String tempPassword = LoginCredValidator.randomP();

			// 🔹 Update password (your current design)
			repo.updateOtpAndPassword(entity.getEmailId(),
                    passwordEncoder.encode(tempPassword),
                    entity.getOtpCreationTime());
			

			String subject = "Registration";
			String name = entity.getFullName();

			String body = emailutils.readEmailBody("new-account.html", name, tempPassword);

			emailutils.sendEmailAsync(entity.getEmailId(), subject, body);

			return true;

		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public void logLoginSuccess(String email, String ip)
	{
		String userId = repo.findUserNoByEmailId(email);
		LoginUser login = loginRepo.findByUserNo(userId);

		if (login == null)
		{
			login = new LoginUser();
			login.setUserNo(userId);
		}

		login.setIpAddress(ip);
		login.setUpdatedDate(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")));
		login.setLockedStatus("Y");
		login.setLoggedStatus("Y");

		loginRepo.save(login);
	}
	
	@Override
	public boolean isLoginBlocked(String email) {

	    String userId = repo.findUserNoByEmailId(email);
	    if (userId == null) {
	        return false; // user not found → let auth handle it
	    }

	    LoginUser login = loginRepo.findByUserNo(userId);
	    if (login == null) {
	        return false; // never logged in before → allow login
	    }

	    // ❌ If already logged in
	    return "Y".equals(login.getLockedStatus())
	        || "Y".equals(login.getLoggedStatus());
	}



	@Transactional
    public void logoutUser(String jwtToken) {

        //  Extract JTI + expiry
//        String jti = jwt.extractJti(jwtToken);
//        Date expiration = jwt.extractExpiration(jwtToken);
//
//        // Blacklist by JTI
//        jwtBlacklistService.addToBlacklist(jti, expiration);

        // Update login table
        String username = jwt.extractUsername(jwtToken);
        String userId = repo.findUserNoByEmailId(username);

        LoginUser login = loginRepo.findByUserNo(userId);
        if (login != null) {
            login.setLockedStatus("N");
            login.setLoggedStatus("N");
            loginRepo.save(login);
        }
    }

	@Override
	public boolean activateUser(ActivateAccount active)
	{
		String validationResult = LoginCredValidator.userP(active.getNewPassword());

		if (validationResult.startsWith("Password Rejected"))
		{
			return false;

		}
		UserEntity entity = new UserEntity();
		entity.setEmailId(active.getEmailId());
		entity.setPassword(active.getTempPassword());

		Example<UserEntity> of = Example.of(entity);
		List<UserEntity> findAll = repo.findAll(of);

		if (findAll.isEmpty())
		{
			return false;
		} else
		{
			UserEntity us = findAll.get(0);
			us.setPassword(active.getNewPassword());
			us.setAccStatus(false);

			repo.save(us);
			return true;

		}
	}

	@Override
	public List<UserData> getAllUsers()
	{
		List<UserEntity> findAll = repo.findAll();

		List<UserData> user = new ArrayList<>();
		for (UserEntity um : findAll)
		{
			UserData u = new UserData();
			BeanUtils.copyProperties(um, user);
			user.add(u);
		}
		return user;
	}

//	@Override
//	public String getUserById(String emailid)
//	{
//		
//
//		Optional<UserEntity> findById = repo.findByEmailId(session.getLoggedInUsername());
//		if (findById.isPresent())
//		{
//			UserData user = new UserData();
//			UserEntity planMaster = findById.get();
//			BeanUtils.copyProperties(planMaster, user);
//			return user;
//		}
//		return null;
//	}

	@Override
	public boolean changeStatus(Integer userId, boolean accStatus)
	{
		Optional<UserEntity> chnageById = repo.findById(userId);
		if (chnageById.isPresent())
		{
			UserEntity entity = chnageById.get();
			entity.setAccStatus(accStatus);
			repo.save(entity);
			return true;
		}
		return false;
	}

//	@Override
//	@Scheduled(fixedRate = 600000000) // runs every 60 seconds
//	public void deletePassword()
//	{
//		LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(5);
//		repo.clearExpiredOtp(expiryTime);
//		System.out.println("♻ Otp Deleted.");
//	}

	@Override
	public String getUserById(String email)
	{
		return null;
	}


	@Override
	public UserData getUserByEmail(String emailId)
	{
		return null;
	}





	
	
}
