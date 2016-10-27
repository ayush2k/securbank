package securbank.models;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
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
	private Long accountNumber;
	
	/** multiple account can be associated with an user	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = false)
	private User user;
	
	/** balance on the account */
	@NotNull
	private Double balance;
	
	@NotNull
	private String type;
	
	@NotNull
	@Column(name = "createdOn", updatable = false)
	private LocalDateTime createdOn;
	
	@NotNull
	@Column(name = "active", columnDefinition = "BIT")
	private Boolean active;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "account")
	private Set<CreditCard> creditCards = new HashSet<CreditCard>(0);
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "account")
	private Set<Transaction> transactions = new HashSet<Transaction>(0);
	
	private static final Random generator = new Random();
	
	public Account() {
		
	}

	/**
	 * @param accountNumber
	 * @param user
	 * @param balance
	 * @param type
	 * @param createdOn
	 * @param active
	 * @param creditCards
	 * @param transactions
	 */
	public Account(Long accountNumber, User user, Double balance, String type, LocalDateTime createdOn, Boolean active,
			Set<CreditCard> creditCards, Set<Transaction> transactions) {
		super();
		this.accountNumber = accountNumber;
		this.user = user;
		this.balance = balance;
		this.type = type;
		this.createdOn = createdOn;
		this.active = active;
		this.creditCards = creditCards;
		this.transactions = transactions;
	}

	/**
	 * @return the accountNumber
	 */
	public Long getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the balance
	 */
	public Double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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

	/**
	 * @return the creditCards
	 */
	public Set<CreditCard> getCreditCards() {
		return creditCards;
	}

	/**
	 * @param creditCards the creditCards to set
	 */
	public void setCreditCards(Set<CreditCard> creditCards) {
		this.creditCards = creditCards;
	}

	/**
	 * @return the transactions
	 */
	public Set<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", user=" + user + ", balance=" + balance + ", type=" + type
				+ ", createdOn=" + createdOn + ", active=" + active + ", creditCards=" + creditCards + ", transactions="
				+ transactions + "]";
	}

	@PrePersist
	private void onCreate() {
		this.accountNumber = new Long(generator.nextInt(900000000) + 100000000);
	}
	
}
