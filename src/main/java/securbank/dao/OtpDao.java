package securbank.dao;

import java.util.UUID;
import securbank.models.Otp;
import securbank.models.User;

/**
 * @author Joel Mascarenhas
 *
 */
public interface OtpDao extends BaseDao<Otp, UUID> {
	public Otp findOtpByUser(User user);
}