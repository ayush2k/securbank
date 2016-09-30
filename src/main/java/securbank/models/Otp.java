package securbank.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.LocalDateTime;


/**
 * @author Joel Mascarenhas
 *
 */

/**
 * @author Joel
 *
 */
@Entity
@Table(name = "Otp")
public class Otp {

	/**
	 * @param user
	 * @param OtpId
	 * @param code
	 * @param active
	 * @param createdOn
	 */
	public Otp(User user, Integer OtpId, Integer code, Boolean active,LocalDateTime createdOn,LocalDateTime expiryTime) {
		super();
		this.user = user;
		this.OtpId = OtpId;
		this.code = code;
		this.active = active;
		this.createdOn = createdOn;
		this.expiryTime = expiryTime;
	}

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@NotNull
	@Column(name = "OtpId", unique = true, columnDefinition = "BINARY(16)")
	private Integer OtpId;
	
	@NotNull
	@Column(name = "code", updatable = false)
	private Integer code;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = false)
	private User user;
	
	@NotNull
	@Column(name = "active", nullable = false, columnDefinition = "BIT")
	private Boolean active = true;
	
	@NotNull
	@Column(name = "createdOn", nullable = false, updatable = false)
	private LocalDateTime createdOn;
	
	@NotNull
	@Column(name = "createdOn", nullable = false, updatable = false)
	private LocalDateTime expiryTime;
	
	public Otp() {
	}
	
	/**
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return
	 */
	public LocalDateTime getExpiryTime() {
		return expiryTime;
	}

	/**
	 * @param expiryTime
	 */
	public void setExpiryTime(LocalDateTime expiryTime) {
		this.expiryTime = expiryTime;
	}
	
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
	public int getOtpId() {
		return OtpId;
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
	 * @param OtpId
	 */
	public void setOtpId(int OtpId) {
		this.OtpId = OtpId;
	}

	@Override
	public String toString() {
		return "Otp [OtpId=" + OtpId + ", code=" + code + ", user=" + user + ", active=" + active + ", createdOn="
				+ createdOn + ", expiryTime=" + expiryTime + "]";
	}

	

}

