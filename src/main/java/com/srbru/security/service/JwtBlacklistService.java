package com.srbru.security.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.srbru.entity.BlacklistedToken;
import com.srbru.repo.BlacklistedTokenRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtBlacklistService
{

	private BlacklistedTokenRepository repo;

	 @Transactional
	    public void addToBlacklist(String token, Date expiryDate) {
	        if (!repo.existsById(token)) {
	            repo.save(new BlacklistedToken(token, expiryDate));
	        }
	    }

	    public boolean isBlacklisted(String jti) {
	        return repo.existsById(jti);
	    }

    // Auto delete expired tokens
//    @Scheduled(cron = "0 0 * * * *")  // Every 1 hour
//    public void removeExpiredTokens() {
//        List<BlacklistedToken> all = repo.findAll();
//
//        Date now = new Date();
//        all.stream()
//           .filter(t -> t.getExpiryDate().before(now))
//           .forEach(t -> repo.delete(t));
//    }
}
