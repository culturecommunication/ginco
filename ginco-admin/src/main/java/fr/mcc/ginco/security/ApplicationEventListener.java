package fr.mcc.ginco.security;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import fr.mcc.ginco.log.Log;


public class ApplicationEventListener implements ApplicationListener<ApplicationEvent> {

	@Log
	private Logger log;
	 
	@Inject
	@Named("lockoutService")
	private LockoutService lockoutService;
	
	
	private void HandleFailureEvent(String username, long timestamp)
	{
		if (!lockoutService.isLockedOut(username)) {
			lockoutService.notifyLoginFailure(username, timestamp);
		} else 
		{
			log.warn("Account "+username+" is locked...");
		}
	}
	
	private void registerSuccessLogin(String username)
	{
		this.lockoutService.notifyLoginSuccess(username);
	}
	 
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// TODO Auto-generated method stub
		if (event instanceof AuthenticationSuccessEvent) {
			registerSuccessLogin(((AbstractAuthenticationEvent) event).getAuthentication().getName());
		}
		if (event instanceof AbstractAuthenticationFailureEvent) {
			String username = ((AbstractAuthenticationFailureEvent) event).getAuthentication().getName();
			String cause = ((AbstractAuthenticationFailureEvent) event).getException().toString();
			log.warn("Failed authentication for user '" + username + " caused by " + cause);
			if (event instanceof AuthenticationFailureBadCredentialsEvent) {
				this.HandleFailureEvent(username, event.getTimestamp()); 
			}
		}
	}

}
