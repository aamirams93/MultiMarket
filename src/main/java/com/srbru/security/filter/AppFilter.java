package com.srbru.security.filter;

import java.io.IOException;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.srbru.security.service.JwtBlacklistService;
import com.srbru.security.service.JwtService;
import com.srbru.service.MyUserDetailsService;
import com.srbru.security.rate.RateLimitService;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AppFilter extends OncePerRequestFilter
{

	private final JwtService jwtService;
	private final MyUserDetailsService userDetailsService;
	private final JwtBlacklistService blacklistService;
	private final RateLimitService rateLimitService;

	private static final Set<String> RATE_LIMITED_APIS = Set.of("/api/v1/auth/login", "/api/v1/auth/add",
			"/api/v1/auth/motp"

	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException
	{

		String uri = request.getRequestURI();


		// 1️⃣ Rate limit only selected APIs
		if (RATE_LIMITED_APIS.contains(uri))
		{

			String ip = request.getRemoteAddr();
			Bucket bucket = rateLimitService.resolveBucket(ip, uri);

			if (bucket != null && !bucket.tryConsume(1))
			{
				response.setStatus(429);
				response.getWriter().write("Too many requests from this IP");
				return;
			}
			// No JWT needed for auth APIs
			chain.doFilter(request, response);
			return;
		}

		// 2️⃣ JWT processing for secured APIs
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer "))
		{

			String token = authHeader.substring(7);

			try
			{
				String username = jwtService.extractUsername(token);

				String jti = jwtService.extractJti(token);
				if (jti != null && blacklistService.isBlacklisted(jti))
				{
					sendUnauthorized(response, "Token revoked");
					return;
				}

				if (SecurityContextHolder.getContext().getAuthentication() == null)
				{

					UserDetails userDetails = userDetailsService.loadUserByUsername(username);

					if (!jwtService.validateToken(token, userDetails))
					{
						sendUnauthorized(response, "Invalid token");
						return;
					}

					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());

					SecurityContextHolder.getContext().setAuthentication(auth);
				}

			} catch (Exception e)
			{
				sendUnauthorized(response, "Invalid JWT");
				return;
			}
		}

		chain.doFilter(request, response);
	}

	private void sendUnauthorized(HttpServletResponse response, String msg) throws IOException
	{

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write("""
				{
				  "status": 401,
				  "error": "Unauthorized",
				  "message": "%s"
				}
				""".formatted(msg));
	}
}

