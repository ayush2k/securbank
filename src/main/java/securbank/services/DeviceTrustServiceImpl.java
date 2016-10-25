package securbank.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import securbank.dao.DeviceTrustDao;
import securbank.dao.UserDao;
import securbank.models.DeviceTrust;
import securbank.models.User;

@Service("deviceTrust")
public class DeviceTrustServiceImpl implements DeviceTrustService{
	@Autowired
	UserDao userDao;

	@Autowired 
	DeviceTrustDao deviceTrustDao;
	
	@Override
	public UUID findUserElementFromUsername(String userName) {
		User user = new User();
		user = userDao.findByUsernameOrEmail(userName);
		UUID userIdFromUserName = user.getUserId();
		return userIdFromUserName;
	}
	
	public List<DeviceTrust> findMacAddresses(String userName){
		List<DeviceTrust> listOfMacs = deviceTrustDao.findByUserName(userName);
		return listOfMacs;
			
	}
 	 
}
