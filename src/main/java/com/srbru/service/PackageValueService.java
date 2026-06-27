package com.srbru.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.srbru.binding.PackageBinding;
import com.srbru.binding.PaymentBinding;
import com.srbru.binding.PaymetUpdateRequest;
import com.srbru.entity.PackageLevel;
import com.srbru.entity.PackageValue;
import com.srbru.entity.UserEntity;
import com.srbru.exception.BusinessException;
import com.srbru.repo.PackLevelRepo;
import com.srbru.repo.PackRepo;
import com.srbru.repo.UserRepo;
import com.srbru.utils.ImageUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PackageValueService
{
	private final PackRepo prepo;

	private final UserRepo crepo;

	private final PackLevelRepo levelRepo;

	@Transactional
	public PackageValue fetch(PackageBinding p, @AuthenticationPrincipal UserDetails user)
	{

		// Validate input
		if (p.getPackLevel() == null || p.getPackLevel().trim().isEmpty())
		{
			throw new BusinessException("Package level is required", "Package level cannot be null");
		}

		// Fetch customer
		UserEntity customer = crepo.findByEmailId(user.getUsername())
				.orElseThrow(() -> new BusinessException("Customer not found", "Customer not found"));

		// Fetch package level
		PackageLevel level = levelRepo.findByPackageNameIgnoreCase(p.getPackLevel())
				.orElseThrow(() -> new BusinessException("Package level not found", "Package level not found"));

		// Create package value
		PackageValue pc = new PackageValue();

		// ✅ FIXED: Safe comparison with null checks
		if (p.getPackLevel().equalsIgnoreCase(level.getPackageName())
				&& p.getPackAmount().compareTo(level.getPackageTotalAmount()) == 0)
		{

			pc.setPackLevel(p.getPackLevel());
			pc.setPackAmount(level.getPackageTotalAmount());
		} else
		{
			throw new BusinessException("Invalid package amount",
					String.format("Package '%s' requires exact amount of %.2f, but you sent %.2f", p.getPackLevel(),
							level.getPackageTotalAmount(), p.getPackAmount()));
		}

		// Set package details
		pc.setPackStatus("PENDING");
		pc.setPackDate(new Date());
		pc.setDailyAmountUpdate(LocalDate.now());
		pc.setCustomer(customer);

		// Handle image
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

		return prepo.save(pc);

	}

	@Transactional
	public Page<PaymentBinding> pendingPayment(int page, int size)
	{

		Pageable pageable = PageRequest.of(page, size, Sort.by("packDate").descending());

		Page<PackageValue> pendingPackages = prepo.findByPackStatus("PENDING", pageable);

		if (pendingPackages.isEmpty())
		{
			throw new BusinessException("Pending payment not found", "No pending payments found in the system");
		}

		return pendingPackages.map(pc -> {

			PaymentBinding binding = new PaymentBinding();

			binding.setPid(pc.getPid());
			binding.setFullName(pc.getCustomer().getFullName());
			binding.setMobileNo(pc.getCustomer().getMobileNo());
			binding.setPackLevel(pc.getPackLevel());
			binding.setPackAmount(pc.getPackAmount());
			binding.setPackStatus(pc.getPackStatus());
			binding.setPackDate(pc.getPackDate());

			if (pc.getViewImage() != null)
			{
				binding.setViewImage(Base64.getEncoder().encodeToString(pc.getViewImage()));
			}

			return binding;
		});
	}

	@Transactional
	public String actionPayment(PaymetUpdateRequest p, UserDetails user)
	{

		// Only pending payments can be processed
		Optional<PackageValue> optionalPayment = prepo.findByPid(p.getPid());
		if (optionalPayment.isEmpty())
		{
			throw new BusinessException("Payment not found", "Payment not found");
		}
		PackageValue payment = optionalPayment.get();
		if (!"PENDING".equalsIgnoreCase(payment.getPackStatus()))
		{
			throw new BusinessException("Payment already processed", "Payment already processed");
		}

		// Update status based on action
		if ("approve".equalsIgnoreCase(p.getPackStatus()))
		{ 
			payment.setPackStatus("APPROVED");
			payment.setActionBy(user.getUsername());
			payment.setActionDate(new Date());
			payment.setRejectRemarks(null);

		} else if ("disapprove".equalsIgnoreCase(p.getPackStatus()))
		{
			payment.setPackStatus("DISAPPROVED");
			payment.setRejectRemarks(p.getRejectRemarks());
			payment.setActionBy(user.getUsername());
			payment.setActionDate(new Date());

		} else
		{
			throw new BusinessException("Invalid status", "Status must be approve or disapprove");
		}

		// Save updated payment
		prepo.save(payment);

		return "Payment " + payment.getPackStatus() + " successfully";
	}

	@Transactional
	    public List<PackageLevel> fetchPackLevel() {
		  List<PackageLevel> packages = levelRepo.findAll(Sort.by("lid").ascending());
	        if (packages.isEmpty()) {
	            throw new BusinessException(
	                    "Packages Level not found",
	                    "No Packages Level found in the system");
	        }
	        return packages;


	    }

}
