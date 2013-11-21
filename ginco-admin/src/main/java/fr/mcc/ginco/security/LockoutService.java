package fr.mcc.ginco.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service("lockoutService")
public class LockoutService {
	
	private Map<String, LockoutData> lockMap = new HashMap<String, LockoutData>();
	 
	private static class LockoutData {
		private int attempts;
		private long lastMillis;
	}
	
	@Value("${login.max.attempts}")
	private int defaultMaxAttemps;
	
	public int getMaxAttemps() {
		return defaultMaxAttemps;
	}

	@Value("${login.lockout.seconds}")
	private long defaultLockoutSeconds;

	public boolean isLockedOut(String username) {
		LockoutData data = getData(username);
		if (data.attempts >= defaultMaxAttemps) {
			long last = System.currentTimeMillis() - data.lastMillis;
			if (last < 1000 * defaultLockoutSeconds) {
				return true;
			}
		}
		return false;
	}
	
	public void notifyLoginFailure(String username, long timestamp)
	{
		LockoutData data = getData(username);
		data.attempts++;
		data.lastMillis = timestamp;
	}
	
	public void notifyLoginSuccess(String username)
	{
		LockoutData data = getData(username);
		data.attempts=0;
	}
	
	private LockoutData getData(String username) {
		LockoutData data = lockMap.get(username);
		if (data == null) {
			data = new LockoutData();
			lockMap.put(username, data);
		}
		return data;
	}
}
