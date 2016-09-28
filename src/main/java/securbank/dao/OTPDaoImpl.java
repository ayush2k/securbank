package securbank.dao;

import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import securbank.models.Otp;


@Repository("otpDao")
public class OTPDaoImpl extends BaseDaoImpl<Otp, UUID> implements OTPDao {
	@Autowired
	EntityManager entityManager; 
	
	/**
     * Returns no of rows inserted into OTP table
     * 
     *	@return count
     */
	@Override
	public int create(UUID userId, Integer code, LocalDateTime createdOn) {
		try {
			return this.entityManager.createNativeQuery("INSERT into Otp values(?,?,?)")
					.setParameter("userId",userId)
					.setParameter("code",code)
					.setParameter("createdOn",createdOn)
					.executeUpdate();
		}
		catch(Exception e) {
			// returns null if failed to insert
			System.out.println(e.getStackTrace());
			return 0;
		}
	}
	
	/**
     * Returns OTP object for given userId and OTP Code
     * 
     *	@return Otp
     */
	@Override
	public Otp getOTP(UUID userId, Integer code) {
		try {
			return (Otp) this.entityManager.createQuery("SELECT otp from Otp otp"
					+ " where otp.userId = :userId and otp.code = code")
					.setParameter("userId", userId)
					.setParameter("code", code)
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no OTP entries found
			return null;
		}
	}

	/**
     * Returns rows updated (Ideally should be 1)
     * 
     *	@return count
     */
	@Override
	public int deactivateOTP(Integer OTPId) {
		try {
			return (int) this.entityManager.createQuery("Update Otp otp set otp.active = 'false'"
					+ " where otp.userId = :userId")
					.setParameter("OTPId", OTPId)
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns 999 if no exception caught
			return 9999;
		}
	}

	

}
