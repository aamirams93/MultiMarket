package com.srbru.binding;

import lombok.Data;

/**
 * Represents an account activation request.
 */
@Data
public class ActivateAccount
{
	private String emailId;

	private String tempPassword;

	private String newPassword;

	private String confirmPassword;

	/**
	 * Default constructor for ActivateAccount.
	 */
	public ActivateAccount()
	{
		
		// No-args constructor
	}

}
