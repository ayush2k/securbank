package securbank.dao;

import java.util.List;

import securbank.models.DeviceTrust;

public interface DeviceTrustDao{
	
	public List<DeviceTrust> findByUserName(String userName);

}
