package securbank.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import securbank.models.DeviceTrust;
@Repository("DeviceTrustDao")
public class DeviceTrustDaoImpl implements DeviceTrustDao{
	@Autowired
	EntityManager entityManager;

	@Override
	public List<DeviceTrust> findByUserName(String userName) {
		try {
			return this.entityManager.createQuery("SELECT deviceTrust from DeviceTrust deviceTrust where deviceTrust.userId = :userID", DeviceTrust.class)
					.setParameter("userID", userName)
					.getResultList();
		}
		catch(NoResultException e) {
			// returns null if no user if found
			return null;
		}
	}	
}
