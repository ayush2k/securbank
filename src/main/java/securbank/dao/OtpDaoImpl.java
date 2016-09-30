package securbank.dao;

import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import securbank.models.Otp;
import securbank.models.User;


@Repository("otpDao")
public class OtpDaoImpl extends BaseDaoImpl<Otp, UUID> implements OtpDao {
	@Autowired
	EntityManager entityManager; 
	
	/**
     * Returns OTP object for given userId and OTP Code
     * 
     *	@return Otp
     */
	@Override
	public Otp findOtpByUser(User user) {
		try {
			// get the latest one by createdOn
			return (Otp) this.entityManager.createQuery("SELECT otp from Otp otp"
					+ " where otp.userId = :userId AND otp.expiryTime >= now() AND otp.active = TRUE")
					.setParameter("userId",user.getUserId())
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no OTP entries found
			return null;
		}
	}
}

