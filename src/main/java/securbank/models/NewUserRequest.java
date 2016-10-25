/**
 * 
 */
package securbank.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;
import org.joda.time.LocalDateTime;

/**
 * @author Ayush Gupta
 *
 */
@Entity
@Table(name = "NewUserRequest")
public class NewUserRequest {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "newUserRequestId", unique = true, columnDefinition = "BINARY(16)")
	private UUID newUserRequestId;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
	private String role;
	
	@NotNull
	@Column(name = "createdOn", updatable = false)
	private LocalDateTime createdOn;

	@NotNull
	@Column(name = "expireOn", updatable = false)
	private LocalDateTime expireOn;
	
	@NotNull
	@Column(name = "active", columnDefinition = "BIT")
	private Boolean active;

	/**
	 * @param newUserRequestId
	 * @param email
	 * @param role
	 * @param createdOn
	 * @param expireOn
	 * @param active
	 */
	public NewUserRequest(UUID newUserRequestId, String email, String role, LocalDateTime createdOn, LocalDateTime expireOn,
			Boolean active) {
		super();
		this.newUserRequestId = newUserRequestId;
		this.email = email;
		this.role = role;
		this.createdOn = createdOn;
		this.expireOn = expireOn;
		this.active = active;
	}

	public NewUserRequest() {
		
	}
	
	/**
	 * @return the newUserRequestId
	 */
	public UUID getNewUserRequestId() {
		return newUserRequestId;
	}

	/**
	 * @param newUserRequestId the newUserRequestId to set
	 */
	public void setNewUserRequestId(UUID newUserRequestId) {
		this.newUserRequestId = newUserRequestId;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the createdOn
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the expireOn
	 */
	public LocalDateTime getExpireOn() {
		return expireOn;
	}

	/**
	 * @param expireOn the expireOn to set
	 */
	public void setExpireOn(LocalDateTime expireOn) {
		this.expireOn = expireOn;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NewUserRequest [newUserRequestId=" + newUserRequestId + ", email=" + email + ", role=" + role + ", createdOn="
				+ createdOn + ", expireOn=" + expireOn + ", active=" + active + "]";
	}
}
	
