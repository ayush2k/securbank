package securbank.services;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import securbank.dao.UserDao;
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
		logger.info("Updating login time for User");
		
		return user;
	}


	@Override
	public String getRedirectUrlFromRole(String role) {
		// TODO Auto-generated method stub
		String targetUrl = "";
        if(role.contains("ADMIN")) {
            targetUrl = "/admin/details";
        } else if(role.contains("MANAGER")) {
            targetUrl = "/manager/details";
        } else if(role.contains("EMPLOYEE")) {
            targetUrl = "/manager/details";
        } else if(role.contains("INDIVIDUAL")|role.contains("MERCHANT")) {
            targetUrl = "/user/details";
        }
        return targetUrl;
	}

	
}
