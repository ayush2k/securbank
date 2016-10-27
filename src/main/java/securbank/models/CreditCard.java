package securbank.models;

import org.joda.time.LocalDateTime;

import org.joda.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Shivani Jhunjhunwala
 *
 */
@Entity
@Table(name = "creditCard")
public class CreditCard {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@NotNull
	@Column(name = "ccId", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	private UUID ccId;

	/**
	 * Multiple credit cards may map to the same account, but only one of those
	 * credit cards may be active at any time.
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "accountNumber")
	private Account account;

	@NotNull
	@Column(name = "apr", nullable = false)
	private Double apr;

	@NotNull
	@Column(name = "max_limit", nullable = false)
	private Double maxLimit;

	@NotNull
	@Column(name = "active", nullable = false, columnDefinition = "BIT")
	private Boolean active;

	@NotNull
	@Column(name = "created_on", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@NotNull 
	private LocalDate statementGeneration;
	
	@Transient
	private Double balance;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cc")
	private Set<CreditCardStatement> statements = new HashSet<CreditCardStatement>(0);
	
	public CreditCard() {
		
	}
	
	/**
	 * @param ccId
	 * @param account
	 * @param apr
	 * @param maxLimit
	 * @param active
	 * @param createdOn
	 * @param statementGeneration
	 * @param balance
	 * @param statements
	 */
	public CreditCard(UUID ccId, Account account, Double apr, Double maxLimit, Boolean active, LocalDateTime createdOn,
			LocalDate statementGeneration, Double balance, Set<CreditCardStatement> statements) {
		super();
		this.ccId = ccId;
		this.account = account;
		this.apr = apr;
		this.maxLimit = maxLimit;
		this.active = active;
		this.createdOn = createdOn;
		this.statementGeneration = statementGeneration;
		this.balance = balance;
		this.statements = statements;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CreditCard [ccId=" + ccId + ", account=" + account + ", apr=" + apr + ", maxLimit=" + maxLimit
				+ ", active=" + active + ", createdOn=" + createdOn + ", statementGeneration=" + statementGeneration
				+ ", balance=" + balance + ", statements=" + statements + "]";
	}

	/**
	 * @return the ccId
	 */
	public UUID getCcId() {
		return ccId;
	}

	/**
	 * @param ccId the ccId to set
	 */
	public void setCcId(UUID ccId) {
		this.ccId = ccId;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return the apr
	 */
	public Double getApr() {
		return apr;
	}

	/**
	 * @param apr the apr to set
	 */
	public void setApr(Double apr) {
		this.apr = apr;
	}

	/**
	 * @return the maxLimit
	 */
	public Double getMaxLimit() {
		return maxLimit;
	}

	/**
	 * @param maxLimit the maxLimit to set
	 */
	public void setMaxLimit(Double maxLimit) {
		this.maxLimit = maxLimit;
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
	 * @return the statementGeneration
	 */
	public LocalDate getStatementGeneration() {
		return statementGeneration;
	}

	/**
	 * @param statementGeneration the statementGeneration to set
	 */
	public void setStatementGeneration(LocalDate statementGeneration) {
		this.statementGeneration = statementGeneration;
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
	 * @return the statements
	 */
	public Set<CreditCardStatement> getStatements() {
		return statements;
	}

	/**
	 * @param statements the statements to set
	 */
	public void setStatements(Set<CreditCardStatement> statements) {
		this.statements = statements;
	}

	
	/**
	 * Sets the created date/time to the current timestamp immediately before
	 * the credit card is inserted.
	 */
	@PrePersist
	protected void onCreate() {
		this.createdOn = LocalDateTime.now();
		this.statementGeneration = LocalDate.now().withYear(2000);
	}
}
