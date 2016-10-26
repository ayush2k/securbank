package securbank.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
=======
import org.springframework.core.env.Environment;
>>>>>>> add523b1404f9082075e9aa3eb14df6bfc5b952f
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import securbank.dao.UserDao;
<<<<<<< HEAD
import securbank.models.DeviceTrust;
=======
import securbank.models.LoginAttempt;
>>>>>>> add523b1404f9082075e9aa3eb14df6bfc5b952f
import securbank.models.User;
import securbank.models.Verification;

/**
 * @author Ayush Gupta
 *
 */
@Transactional
@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	private UserDao userDao;

	
	@Autowired 
	private PasswordEncoder encoder;
	
	@Autowired
	private EmailService emailService;
	
<<<<<<< HEAD
	@Autowired
	DeviceTrustService deviceTrustService;
	
=======
	private SimpleMailMessage message;
	
	@Autowired
	private Environment env;
>>>>>>> add523b1404f9082075e9aa3eb14df6bfc5b952f
	
	@Autowired
	VerificationService verificationService;
	
	@Autowired
	private ForgotPasswordService forgotPasswordService;

	private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
	
	/**
     * Verify the username and password for current user
     * 
     * @param usernane
     *            The username or email of user
     * @param password
     *            The password of user
     * @return user
     */
	@Override
	public User verifyUser(String username, String password) {
		User user = userDao.findByUsernameOrEmail(username);
		if (user == null) {
			return null;
		}
		
		logger.info("Verifying username and password");
		
		if (!BCrypt.checkpw(password, user.getPassword())) {
			//User found-Incorrect password, counter incremented 
			LoginAttempt attempt= user.getLoginAttempt();
			attempt.setCounter(attempt.getCounter() + 1);
			if(attempt.getCounter() == 3){
				user.setActive(false);
				
				
				Verification verification = verificationService.createVerificationCodeByType(user, "lock");				
				if (verification != null) {
					// Send email message
					message = new SimpleMailMessage();
					message.setText(env.getProperty("account.reactivate.body").replace(":id:",verification.getVerificationId().toString()));
					message.setSubject(env.getProperty("account.reactivate.subject"));
					message.setTo(user.getEmail());
					emailService.sendEmail(message);
					logger.info("POST request : Sending link to reset password and reactivate account.");
				}
			}
			user.setLoginAttempt(attempt);
			userDao.update(user);
			return null;
		}
		
		return user;
	}
	
	
	/**
     * Updates the last login time of user
     * 
     * @param user
     *            The User object of user
     * @return user
     */
	@Override
	public User updateLoginTime(User user) {
		user.setLastLogin(LocalDateTime.now());
		//Login Successful, LoginAttempt counter reset to 0
		LoginAttempt attempt = user.getLoginAttempt();
		if(attempt==null)
			attempt=new LoginAttempt(user, 0, LocalDateTime.now());
		attempt.setCounter(0);
		attempt.setLastUpdated(LocalDateTime.now());
		user.setLoginAttempt(attempt);

		logger.info("Resetting LoginAteempt counter to 0");
		
		user = userDao.update(user);
		if (user == null) {
			return null;
		}
		logger.info("Updating login time for User");
		
		return user;
	}

<<<<<<< HEAD
	@Override
	public User verifyMacAddress(String username, String macAddress) {
		User user = userDao.findByUsernameOrEmail(username);
		DeviceTrust deviceTrust = new DeviceTrust();
		List<DeviceTrust> listObtained = deviceTrustService.findMacAddresses(username);
		if(listObtained.contains(macAddress)){
			return user;
		}
		else{
			SimpleMailMessage message = new SimpleMailMessage();
			message.setText("Your account has been logged in using new device");
			message.setSubject("New sign-in");
			message.setTo(user.getEmail());
			emailService.sendEmail(message);
			deviceTrust.setUser(user);
			deviceTrust.setMacAddress(macAddress);
		}
		return user;
	}
=======

	@Override
	public String getRedirectUrlFromRole(String role) {
		// TODO Auto-generated method stub
		String targetUrl = "";
        if(role.contains("ADMIN")) {
            targetUrl = "/admin/details";
        } else if(role.contains("MANAGER")) {
            targetUrl = "/manager/details";
        } else if(role.contains("EMPLOYEE")) {
            targetUrl = "/employee/details";
        } else if(role.contains("INDIVIDUAL")) {
            targetUrl = "/user/details";
        } else if(role.contains("MERCHANT")) {
            targetUrl = "/merchant/details";
        }
        return targetUrl;
	}

>>>>>>> add523b1404f9082075e9aa3eb14df6bfc5b952f
	
}
