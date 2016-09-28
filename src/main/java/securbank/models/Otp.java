package securbank.models;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.LocalDateTime;


/**
 * @author Joel Mascarenhas
 *
 */

@Entity
@Table(name = "Otp")
public class Otp {

	/**
	 * @param userId
	 * @param OTPId
	 * @param code
	 * @param active
	 * @param createdOn
	 */

	public Otp(UUID userId, Integer OTPId, Integer code, Boolean active,LocalDateTime createdOn) {
		super();
		this.userId = userId;
		this.OTPId = OTPId;
		this.code = code;
		this.active = active;
		this.createdOn = createdOn;
	}

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@NotNull
	@Column(name = "OTPId", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	private Integer OTPId;
	
	@NotNull
	@Column(name = "code", nullable = false, updatable = false)
	private Integer code;
	
	@Column(name = "userId", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	@NotNull
	private UUID userId;
	
	@NotNull
	@Column(name = "active", nullable = false, columnDefinition = "BIT")
	private Boolean active = true;
	
	@NotNull
	@Column(name = "createdOn", nullable = false, updatable = false)
	private LocalDateTime createdOn;
	
	
	/**
	 * @return
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}


	/**
	 * @param createdOn
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	
	/**
	 * @return
	 */
	public int getOTPId() {
		return OTPId;
	}


	/**
	 * @return
	 */
	public UUID getUserId() {
		return userId;
	}


	/**
	 * @return OTP code
	 */
	public int getCode() {
		return code;
	}


	/**
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}


	/**
	 * @param userId
	 */
	public void setUserId(UUID userId) {
		this.userId = userId;
	}


	/**
	 * @return status
	 */
	public Boolean getActive() {
		return active;
	}


	/**
	 * @param active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}


	/**
	 * @param oTPId
	 */
	public void setOTPId(int oTPId) {
		OTPId = oTPId;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Otp [OTPId=" + OTPId + ", userId=" + userId + ", active=" + active + "]";
	}

	
}

