package securbank.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;
import org.joda.time.LocalDateTime;

/**
 * @author Ayush Gupta
 *
 */
@Entity
@Table(name = "User")
public class User {
	
	/**
	 * @param userId
	 * @param username
	 * @param password
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @param addressLine1
	 * @param addressLine2
	 * @param city
	 * @param state
	 * @param zip
	 * @param createdOn
	 * @param modifiedOn
	 * @param lastLogin
	 * @param active
	 */
	public User(UUID userId, String username, String password, String firstName, String middleName, String lastName,
			String email, String phone, String addressLine1, String addressLine2, String city, String state, String zip,
			LocalDateTime createdOn, LocalDateTime modifiedOn, LocalDateTime lastLogin, Boolean active) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.lastLogin = lastLogin;
		this.active = active;
	}
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@NotNull
	@Column(name = "userId", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	private UUID userId;
	
	@NotNull
	@Size(min = 2, max = 35)
	@Column(name = "username", unique = true, nullable = false)
	private String username;
	
	@NotNull
	@Size(min = 60, max = 60)
	private String password;
	
	@NotNull
	@Size(min = 2, max = 50)
	private String firstName;
	
	@Size(min = 2, max = 50)
	private String middleName;
	
	@NotNull
	@Size(min = 2, max = 50)
	private String lastName;
	
	@NotNull
	@Email
	@Size(min = 2, max = 35)
	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@NotNull
	@Size(min = 10, max = 10)
	@Column(name = "phone", unique = true, nullable = false)
	private String phone;

	@NotNull
	@Size(min = 2, max = 50)
	private String addressLine1;

	@Size(min = 0, max = 50)
	private String addressLine2;

	@NotNull
	@Size(min = 2, max = 35)
	private String city;

	@NotNull
	@Size(min = 2, max = 35)
	private String state;

	@NotNull
	@Size(min = 5, max = 5)
	private String zip;

	@NotNull
	@Column(name = "createdOn", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modifiedOn", nullable = true, updatable = true)
	private LocalDateTime modifiedOn;

	@Column(name = "lastLogin", nullable = true, updatable = true)
	private LocalDateTime lastLogin;

	@NotNull
	@Column(name = "active", nullable = false, columnDefinition = "BIT")
	private Boolean active;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	private Set<Otp> otps = new HashSet<Otp>(0);
	
	
	/**
	 * @return otp
	 */
	public Set<Otp> getOtps() {
		return otps;
	}

	/**
	 * @param otp
	 */
	public void setOtps(Set<Otp> otps) {
		this.otps = otps;
	}

	public User() {
		
	}
	
	/**
	 * @return the userId
	 */
	public UUID getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * @param addressLine1 the addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * @param addressLine2 the addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
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
	 * @return the modifiedOn
	 */
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * @return the lastLogin
	 */
	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	/**
	 * @param lastLogin the lastLogin to set
	 */
	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
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
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", firstName="
				+ firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", email=" + email + ", phone="
				+ phone + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", city=" + city
				+ ", state=" + state + ", zip=" + zip + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn
				+ ", lastLogin=" + lastLogin + ", active=" + active + "]";
	}

	
}
