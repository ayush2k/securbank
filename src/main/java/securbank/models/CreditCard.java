package securbank.models;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Shivani Jhunjhunwala
 *
 */
@Entity
@Table(name = "CreditCard")
public class CreditCard {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@NotNull
	@Column(name = "ccid", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	private UUID ccId;

	@NotNull
	@Column(name = "account_number", nullable = false)
	private Long accountNumber;

	/**
	 * Multiple credit cards may map to the same account, but only one of those
	 * credit cards may be active at any time.
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "account_number", insertable = false, updatable = false)
	private Account account;

	@NotNull
	@Column(name = "apr", nullable = false)
	private Double apr;

	@NotNull
	@Column(name = "max_limit", nullable = false)
	private Double maxLimit;

	@NotNull
	@Column(name = "balance", nullable = false)
	private Double balance;

	@NotNull
	@Column(name = "active", nullable = false, columnDefinition = "BIT")
	private Boolean active;

	@NotNull
	@Column(name = "created_on", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
	private LocalDateTime createdOn;

	/**
	 * @return The credit card id.
	 */
	public UUID getCcId() {
		return ccId;
	}

	/**
	 * @param ccID
	 *            the ccID to set
	 */
	public void setCcID(UUID ccID) {
		this.ccId = ccID;
	}

	/**
	 * @return The account associated with this card.
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return The account number of the account associated with this card.
	 */
	public Long getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Associates this card with the given account.
	 *
	 * @param account
	 *            The new account to associate this card with.
	 */
	public void setAccount(Account account) {
		this.account = account;
		this.accountNumber = account.getAccountNumber();
	}

	/**
	 * @return The annual percentage rate (APR).
	 */
	public Double getApr() {
		return this.apr;
	}

	/**
	 * Sets the annual percentage rate (APR) to the given value.
	 *
	 * @param apr
	 *            The new APR.
	 */
	public void setApr(double apr) {
		this.apr = apr;
	}

	/**
	 * @return The maximum spending limit.
	 */
	public Double getMaxLimit() {
		return maxLimit;
	}

	/**
	 * Sets the maximum spending limit to the given value.
	 *
	 * @param maxLimit
	 *            The new maximum spending limit.
	 */
	public void setMaxLimit(Double maxLimit) {
		this.maxLimit = maxLimit;
	}

	/**
	 * @return The credit card balance.
	 */
	public Double getBalance() {
		return balance;
	}

	/**
	 * Sets the credit card balance to the given balance.
	 *
	 * @param balance
	 *            The new credit card balance.
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	/**
	 * @return The active status.
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * Marks this card as active or inactive.
	 *
	 * @param active
	 *            The new active status.
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return This card's creation timestamp.
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            The new creation datetime.
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "CreditCard [ccid=" + ccId + ", account_number=" + accountNumber + ", apr=" + apr + ", max_limit="
				+ maxLimit + ", balance=" + balance + ", active=" + active + ", created_on=" + createdOn + "]";
	}

	/**
	 * Sets the created date/time to the current timestamp immediately before
	 * the credit card is inserted.
	 */
	@PrePersist
	protected void onCreate() {
		this.createdOn = LocalDateTime.now();
	}
}
