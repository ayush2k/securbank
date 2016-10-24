package securbank.services;

import securbank.models.CreatePasswordRequest;
import securbank.models.ForgotPasswordRequest;
import securbank.models.User;

/**
 * 
 * @author Madhu
 *
 */
public interface ForgotPasswordService {
	public boolean sendEmailForgotPassword(User user);
	public boolean verifyUserAndInfo(User user, ForgotPasswordRequest request);
	public User createUserPassword(User user, CreatePasswordRequest model);
	public User getUserbyUsername(String username);

}
