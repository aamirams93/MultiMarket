package com.srbru.service;

import java.util.List;

import com.srbru.binding.ActivateAccount;
import com.srbru.binding.UserData;

public interface UserService
{
	public boolean saveUser(UserData userData);

	public boolean activateUser(ActivateAccount active);

	public List<UserData> getAllUsers();

	public String getUserById(String email);

	public boolean changeStatus(Integer userId, boolean accStatus);
	
	public UserData getUserByEmail(String emailId); 
	
	public boolean generateEmailOtp(UserData email);
	
	public void logoutUser(String jwtToken);
	
	public void logLoginSuccess(String email, String ip);
	
	public boolean isLoginBlocked(String email);

}
