package securbank.services;

import java.util.List;
import java.util.UUID;

import securbank.models.DeviceTrust;

public interface DeviceTrustService {
		public UUID findUserElementFromUsername(String userName);
		public List<DeviceTrust> findMacAddresses(String userName);
	
}
