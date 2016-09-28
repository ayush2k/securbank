package securbank.dao;

import java.util.UUID;

import org.joda.time.LocalDateTime;

import securbank.models.Otp;
/**
 * @author Joel Mascarenhas
 *
 */
public interface OTPDao extends BaseDao<Otp, UUID> {
	public int create(UUID userId,Integer code,LocalDateTime createdOn);
	public Otp getOTP(UUID userId,Integer code);
	public int deactivateOTP(Integer OTPId);
}
