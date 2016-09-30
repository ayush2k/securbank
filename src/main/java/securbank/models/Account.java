package securbank.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.joda.time.LocalDateTime;

/**
 * 
 * @author Madhu Illuri
 */

@Entity 
@Table(name = "Account")
public class Account {
	
	/** Account number is unique. */
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountNumber;
	
	/** multiple account can be associated with an user	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user", unique = true, nullable = false)
	private User user;
	
	/** balance on the account */
	@NotNull
	private Double balance;
	
	/** account type. Default an account will be checking(0), (1) for savings account	*/
	@NotNull
	@Column(name = "accountType", columnDefinition = "int(1) DEFAULT '0' ")
	private Integer accountType;
	
	@NotNull
	@Column(name = "createdOn", updatable = false)
	private LocalDateTime createdOn;
	
	@NotNull
	@Column(name = "active", columnDefinition = "BIT")
	private Boolean active;
	
	/**
	 * 
	 * @param accountNumber
	 * @param user
	 * @param balance
	 * @param accountType
	 * @param createdOn
	 * @param active
	 */
	public Account(Long accountNumber, User user, double balance, int accountType, LocalDateTime createdOn
			,Boolean active){
		
		super();
		this.accountNumber = accountNumber;
		this.user = user;
		this.balance = balance;
		this.accountType = accountType;
		this.createdOn = createdOn;
		this.active = active;	
		
	}
	
	public Account() {
		
	}
	
	/**
	 * 
	 * @return accountNumber
	 */
	public Long getAccountNumber() {
		return accountNumber;
	}
	
	/**
	 * 
	 * @param accountNumber sets accountNumber
	 */
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	/**
	 * 
	 * @return user
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * 
	 * @param user sets user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 
	 * @return balance
	 */
	public Double getBalance() {
		return balance;
	}
	
	/**
	 * 
	 * @param balance sets balance
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	/**
	 * 
	 * @return accountType
	 */
	public Integer getAccountType() {
		return accountType;
	}
	
	/**
	 * 
	 * @param accountType sets accountType
	 */
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
		
	/**
	 * 
	 * @return createdOn time created on
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	
	/**
	 * 
	 * @param createdOn sets time created on 
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
		
	/**
	 * 
	 * @return active if the account is active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * 
	 * @param active sets account active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return "Account [accountNumber=" + accountNumber + ", user=" + user + ", balance="
				+ balance + ", accountType=" + accountType + ", active=" + active + ", balance=" +
				", createdOn=" + createdOn + "]";
	}

}
