package fr.mcc.ginco.security;

import java.util.HashMap;
import java.util.Map;


public class LockoutService {
	
	private Map<String, LockoutData> lockMap = new HashMap<String, LockoutData>();
	 
	private static class LockoutData {
		int attempts;
		long lastMillis;
	}
	
	private static final int DEFAULT_MAX_ATTEMPTS = 3;
	private static final long DEFAULT_LOCKOUT_SECONDS = 60;

	public boolean isLockedOut(String username) {
		LockoutData data = getData(username);
		if (data.attempts >= DEFAULT_MAX_ATTEMPTS) {
			long last = System.currentTimeMillis() - data.lastMillis;
			if (last < 1000 * DEFAULT_LOCKOUT_SECONDS) {
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
