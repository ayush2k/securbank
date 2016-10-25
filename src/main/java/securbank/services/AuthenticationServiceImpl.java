package securbank.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import securbank.dao.UserDao;
import securbank.models.DeviceTrust;
import securbank.models.User;

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
	
	@Autowired
	DeviceTrustService deviceTrustService;
	
	
	
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
		
		if (!BCrypt.checkpw(password, user.getPassword())) {
			return null;
		};
		
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
		user = userDao.update(user);
		if (user == null) {
			return null;
		}
		
		return user;
	}

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
	
}
