package securbank.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * @author Ayush Gupta
 *
 */
@Entity
@Table(name = "creditCardStatement")
public class CreditCardStatement {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@NotNull
	@Column(name = "statementId", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	private UUID statementId;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "ccId")
	private CreditCard cc;

	@NotNull
	private LocalDate startDate;
	
	@NotNull
	private LocalDate endDate;

	@NotNull
	private LocalDate pendingDate;

	private Double closingBalance;
	
	private String status;
	
	@NotNull
	@Column(name = "created_on", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@Transient
	private List<Transaction> transactions = new ArrayList<Transaction>(0);
	
	public CreditCardStatement() {
		
	}

	/**
	 * @param statementId
	 * @param cc
	 * @param startDate
	 * @param endDate
	 * @param pendingDate
	 * @param closingBalance
	 * @param status
	 * @param createdOn
	 * @param transactions
	 */
	public CreditCardStatement(UUID statementId, CreditCard cc, LocalDate startDate, LocalDate endDate,
			LocalDate pendingDate, Double closingBalance, String status, LocalDateTime createdOn,
			List<Transaction> transactions) {
		super();
		this.statementId = statementId;
		this.cc = cc;
		this.startDate = startDate;
		this.endDate = endDate;
		this.pendingDate = pendingDate;
		this.closingBalance = closingBalance;
		this.status = status;
		this.createdOn = createdOn;
		this.transactions = transactions;
	}

	/**
	 * @return the statementId
	 */
	public UUID getStatementId() {
		return statementId;
	}

	/**
	 * @param statementId the statementId to set
	 */
	public void setStatementId(UUID statementId) {
		this.statementId = statementId;
	}

	/**
	 * @return the cc
	 */
	public CreditCard getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(CreditCard cc) {
		this.cc = cc;
	}

	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the pendingDate
	 */
	public LocalDate getPendingDate() {
		return pendingDate;
	}

	/**
	 * @param pendingDate the pendingDate to set
	 */
	public void setPendingDate(LocalDate pendingDate) {
		this.pendingDate = pendingDate;
	}

	/**
	 * @return the closingBalance
	 */
	public Double getClosingBalance() {
		return closingBalance;
	}

	/**
	 * @param closingBalance the closingBalance to set
	 */
	public void setClosingBalance(Double closingBalance) {
		this.closingBalance = closingBalance;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the transactions
	 */
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CreditCardStatement [statementId=" + statementId + ", cc=" + cc + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", pendingDate=" + pendingDate + ", closingBalance=" + closingBalance
				+ ", status=" + status + ", createdOn=" + createdOn + ", transactions=" + transactions + "]";
	}

	/**
	 * Sets the created date/time to the current timestamp immediately before
	 * the credit card is inserted.
	 */
	@PrePersist
	protected void onCreate() {
		this.createdOn = LocalDateTime.now();
		this.startDate = LocalDate.now();
		this.endDate = LocalDate.now().plusMonths(1);
		this.pendingDate = LocalDate.now().plusMonths(0);
		this.status = "current";
	}
}
