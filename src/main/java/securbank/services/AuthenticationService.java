package securbank.services;

import securbank.models.User;

/**
 * @author Ayush Gupta
 *
 */
public interface AuthenticationService {
	public User verifyUser(String username, String password);
	public User verifyMacAddress(String username, String macAddress);
	public User updateLoginTime(User user);
}
