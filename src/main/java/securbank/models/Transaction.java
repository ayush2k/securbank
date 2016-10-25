package securbank.models;

import java.util.UUID;

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
 * @author Mitikaa Sama
 *
 * Sep 26, 2016
 */

@Entity
@Table(name = "Transaction")

public class Transaction {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@NotNull
	@Column(name = "transactionId", unique = true, nullable = false, columnDefinition = "BINARY(16)")
	private UUID transactionId;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "accountId", nullable = false, updatable = false)
	private Account account;
	
	@NotNull
	@Column(name = "amount", unique = false, nullable = false, updatable = false)
	private double amount;
	
	@NotNull
	@Column(name = "type", unique = false, nullable = false)
	private String type;
	
	@NotNull
	@Column(name = "approvalStatus", unique = false, nullable = false, updatable = true)
	private String approvalStatus;
	
	@NotNull
	@Column(name = "oldBalance", unique = false, nullable = true, updatable = true)
	private double oldBalance;
	
	@NotNull
	@Column(name = "newBalance", unique = false, nullable = true, updatable = true)
	private double newBalance;
	
	@NotNull
	@Column(name = "criticalStatus", unique = false, nullable = false, columnDefinition = "BIT")
	private boolean criticalStatus;
	
	@ManyToOne( optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "transferId", nullable = true)
	private Transfer transfer;
	
	@NotNull
	@Column(name = "createdOn", nullable = false, updatable = false)
	private LocalDateTime createdOn;
	
	@Column(name = "modifiedOn", nullable = true, updatable = true)
	private LocalDateTime modifiedOn;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = true)
	private User modifiedBy;

	@NotNull
	@Column(name = "active", nullable = false, columnDefinition = "BIT")
	private Boolean active;
	
	public Transaction(){
		
	}
	
	/**
	 * @param transactionId
	 * @param accountNumber
	 * @param amount
	 * @param type
	 * @param approvalStatus
	 * @param oldBalance
	 * @param newBalance
	 * @param criticalStatus
	 * @param transferId
	 * @param createdOn
	 * @param modifiedOn
	 * @param modifiedBy
	 * @param active
	 */
	public Transaction(UUID transactionId, Account accountNumber, double amount, 
			String type, String approvalStatus, double oldBalance, double newBalance, Boolean criticalStatus, 
			Transfer transfer, LocalDateTime createdOn, LocalDateTime modifiedOn, 
			User modifiedBy, Boolean active){
		super();
		this.transactionId = transactionId;
		this.account = accountNumber;
		this.amount = amount;
		this.type = type;
		this.approvalStatus = approvalStatus;
		this.oldBalance = oldBalance;
		this.newBalance = newBalance;
		this.criticalStatus = criticalStatus;
		this.transfer = transfer;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.active = active;
		
	}

	
	/**
	 * @return the transfer
	 */
	public Transfer getTransfer() {
		return transfer;
	}


	/**
	 * @return the modifiedOn
	 */
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}


	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}


	/**
	 * @param transfer the transfer to set
	 */
	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}


	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}


	/**
	 * @return the modifiedBy
	 */
	public User getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}


	/**
	 * @return the transactionId
	 */
	public UUID getTransactionId() {
		return transactionId;
	}


	/**
	 * @return the accountNumber
	 */
	public Account getAccount() {
		return account;
	}


	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @return the oldBalance
	 */
	public double getOldBalance() {
		return oldBalance;
	}

	
	/**
	 * @return the newBalance
	 */
	public double getNewBalance() {
		return newBalance;
	}


	/**
	 * @return the criticalStatus
	 */
	public boolean isCriticalStatus() {
		return criticalStatus;
	}


	/**
	 * @return the createdOn
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}


	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}


	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}


	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @param oldBalance the oldBalance to set
	 */
	public void setOldBalance(double oldBalance) {
		this.oldBalance = oldBalance;
	}


	/**
	 * @param newBalance the newBalance to set
	 */
	public void setNewBalance(double newBalance) {
		this.newBalance = newBalance;
	}


	/**
	 * @param criticalStatus the criticalStatus to set
	 */
	public void setCriticalStatus(boolean criticalStatus) {
		this.criticalStatus = criticalStatus;
	}


//	/**
//	 * @param transferId the transferId to set
//	 */
//	public void setTransferId(UUID transferId) {
//		this.transferId = transferId;
//	}


	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}


	/**
	 * @return the status
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}


	/**
	 * @param status the status to set
	 */
	public void setApprovalStatus(String status) {
		this.approvalStatus = status;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", account=" + account + ", amount=" + amount
				+ ", type=" + type + ", oldBalance=" + oldBalance + ", newBalance=" + newBalance + ", criticalStatus="
				+ criticalStatus + ", transfer=" + transfer + ", createdOn=" + createdOn + "]";
	}


}
