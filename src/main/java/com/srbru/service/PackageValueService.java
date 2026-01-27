package com.srbru.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.srbru.binding.PackageBinding;
import com.srbru.entity.PackageValue;
import com.srbru.entity.UserEntity;
import com.srbru.exception.BusinessException;
import com.srbru.repo.PackRepo;
import com.srbru.repo.UserRepo;
import com.srbru.utils.ImageUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PackageValueService
{
	private PackRepo prepo;

	private UserRepo crepo;
	


	    
	@Transactional
	public PackageBinding fetch(PackageBinding p, @AuthenticationPrincipal UserDetails user)
	{
				    
		UserEntity customer = crepo.findByEmailId(user.getUsername())
				.orElseThrow(() -> new BusinessException("Customer not found","Customer not found"));

		PackageValue pc = new PackageValue();
		pc.setPackLevel(p.getPackLevel()); 
		pc.setPackAmount(p.getPackAmount());
		pc.setPackStatus("PENDING");
		pc.setPackDate(LocalDate.now());
		pc.setDailyAmountUpdate(LocalDate.now());
		pc.setCustomer(customer); 

		try
		{
			if (p.getViewImage() != null && !p.getViewImage().isEmpty())
			{
				byte[] original = p.getViewImage().getBytes();
				byte[] compressed = ImageUtil.compressImage(original);
				pc.setViewImage(compressed);
			}
		} catch (IOException e)
		{
			throw new BusinessException("Image upload failed", e.getMessage());
		}

		prepo.save(pc);

		//customer.setPackageLevel(p.getPackLevel());
		crepo.save(customer);

		return p;
	}

	public List<PackageValue> getPackagesByEmailId(String email)
	{
		return prepo.findPackagesByCustomerEmailId(email);
	}
}
