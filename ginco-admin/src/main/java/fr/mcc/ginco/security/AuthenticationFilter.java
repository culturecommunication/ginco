package fr.mcc.ginco.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	private LockoutService lockoutService;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
	                                            HttpServletResponse response) throws AuthenticationException {

		logger.warn("Attempting authentication");
		String username = obtainUsername(request);
		logger.warn("Locked " + username + " : " + this.lockoutService.isLockedOut(username));
		if (this.lockoutService.isLockedOut(username)) {
			throw new LockedException("Too many authentication failures, account is locked");
		}
		return super.attemptAuthentication(request, response);
	}

	public LockoutService getLockoutService() {
		return lockoutService;
	}

	public void setLockoutService(LockoutService lockoutService) {
		this.lockoutService = lockoutService;
	}

}
