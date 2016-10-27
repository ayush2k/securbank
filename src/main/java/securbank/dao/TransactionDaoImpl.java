package securbank.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import securbank.models.Account;
import securbank.models.Transaction;

/**
 * @author Mitikaa Sama
 *
 * Sep 26, 2016
 */

@Repository("transactionDao")
public class TransactionDaoImpl extends BaseDaoImpl<Transaction, UUID> implements TransactionDao{
	@Autowired
	EntityManager entityManager;
	
	
	public TransactionDaoImpl() {
		super(Transaction.class);
	}
	
	/**
     * Returns list of all transactions in the table
     * 
     * @return transactions
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> findAll() {
		return (List<Transaction>) this.entityManager.createQuery("SELECT transaction from Transaction transaction")
				.getResultList();
	}
	
	/**
     * Returns list of transactions in the table filtered by account number
     * 
     * @return transactions
     */
	@Override
	public List<Transaction> findByAccount(Account account) {
		return this.entityManager.createQuery("SELECT transaction from Transaction transaction"
				+ " WHERE transaction.account = :account"
				+ " AND active = true"
				+ " ORDER BY transaction.createdOn DESC", Transaction.class)
				.setParameter("account", account)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> findByAccount(String accountNumber) {
		return (List<Transaction>) this.entityManager.createQuery("SELECT transaction from Transaction transaction"
				+ " where transaction.accountNumber = :accountNumber")
				.setParameter("accountNumber", accountNumber)
				.getResultList();
	}
	
	/**
     * Returns list of transactions in the table filtered by account number and type
     * 
     * @return transactions
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> findByAccountAndType(Long accountNumber, String type) {
		try {
			return (List<Transaction>) this.entityManager.createQuery("SELECT transaction from Transaction transaction"
					+ " where (transaction.accountNumber = :accountNumber AND transaction.type = type)")
					.setParameter("accountNumber", accountNumber)
					.setParameter("type", type)
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no transaction is found
			return null;
		}
	}

	/**
     * Returns list of transactions in the table filtered by critical status of the transaction
     * 
     * @return transactions
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> findByCriticalStatus(Boolean criticalStatus) {
		try {
			return (List<Transaction>) this.entityManager.createQuery("SELECT transaction from Transaction transaction"
					+ " where transaction.criticalStatus = :criticalStatus")
					.setParameter("criticalStatus", criticalStatus)
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no transaction is found
			return null;
		}
	}

	/**
	 Returns list of transactions in the table filtered by the approval status of the transaction
     * 
     * @return transactions
	 */
	@Override
	public List<Transaction> findByApprovalStatus(String approvalStatus) {
		try {
			return this.entityManager.createQuery("SELECT transaction from Transaction transaction"
					+ " where transaction.approvalStatus = :approvalStatus", Transaction.class)
					.setParameter("approvalStatus", approvalStatus)
					.getResultList();
		}
		catch(NoResultException e) {
			// returns null if no transaction is found
			return null;
		}
	}
	
	/**
     * Returns list of transactions in the table filtered by account number and type
     * 
     * @return transactions
     */
	@Override
	public List<Transaction> findPendingByAccountAndType(Account account, String type) {
		try {
			return this.entityManager.createQuery("SELECT transaction from Transaction transaction "
					+ "where transaction.account = :account AND transaction.type = :type AND transaction.approvalStatus = :approvalStatus", Transaction.class)
					.setParameter("account", account)
					.setParameter("type", type)
					.setParameter("approvalStatus", "Pending")
					.getResultList();
		}
		catch(NoResultException e) {
			// returns null if no transaction is found
			return null;
		}
	}

	/**
     * Returns list of transactions in the table filtered by critical status of the transaction
     * 
     * @return transactions
     */
	@Override
	public List<Transaction> findPendingByCriticalStatus(Boolean criticalStatus) {
		try {
			return  this.entityManager.createQuery("SELECT transaction from Transaction transaction"
					+ " where transaction.criticalStatus = :criticalStatus", Transaction.class)
					.setParameter("criticalStatus", criticalStatus)
					.setParameter("approvalStatus", "Pending")
					.getResultList();
		}
		catch(NoResultException e) {
			// returns null if no transaction is found
			return null;
		}
	}

	/**
     * Returns list of transactions in the table filtered by account number
     * 
     * @return transactions
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> findPendingByAccount(Long accountNumber) {
		try {
			return (List<Transaction>) this.entityManager.createQuery("SELECT transaction from Transaction transaction"
					+ " where transaction.accountNumber = :accountNumber AND transaction.type = type AND approvalStatus = :approvalStatus")
					.setParameter("accountNumber", accountNumber)
					.setParameter("approvalStatus", "Pending")
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no transaction is found
			return null;
		}
	}

	@Override
	public Transaction findPendingTransactionByAccount(Long accountNumber) {
		try {
			System.out.println("Inside findPendingTransactionByAccount");
			return  this.entityManager.createQuery("SELECT transaction from Transaction transaction"
					+ " where transaction.account.accountNumber = :accountNumber AND transaction.approvalStatus = :approvalStatus", Transaction.class)
					.setParameter("accountNumber", accountNumber)
					.setParameter("approvalStatus", "Pending")
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no transaction is found
			return null;
		}
	}

	/**
     * Returns list of transactions in the table filtered by start and end date
     * @param account
     * 			the account to query
     * @param start
     * 			the start data for whjch to query
     * @param end
     * 			the end data for whjch to query
     * @return transactions
     */
	@Override
	public List<Transaction> findByAccountAndDateRange(Account account, LocalDateTime start, LocalDateTime end) {
		return this.entityManager.createQuery("SELECT transaction FROM Transaction transaction"
				+ " WHERE transaction.account = :account"
				+ " AND transaction.createdOn >= :start"
				+ " AND transaction.createdOn < :end"
				+ " AND transaction.active = true"
				+ " ORDER BY transaction.createdOn DESC", Transaction.class)
				.setParameter("account", account)
				.setParameter("start", start)
				.setParameter("end", end)
				.getResultList();
	}

	@Override
	public Double findSumByAccountAndDateRange(Account account, LocalDateTime start, LocalDateTime end) {
		return this.entityManager.createQuery("SELECT SUM(transaction.amount) from Transaction transaction"
				+ " WHERE transaction.account = :account"
				+ " AND transaction.createdOn >= :start"
				+ " AND transaction.createdOn < :end"
				+ " AND transaction.active = true", Double.class)
				.setParameter("account", account)
				.setParameter("start", start)
				.setParameter("end", end)
				.getSingleResult();
	}

	@Override
	public List<Transaction> findNonCriticalByApprovalStatus(String status) {
		try {
			return this.entityManager.createQuery("SELECT transaction from Transaction transaction"
					+ " where transaction.approvalStatus = :approvalStatus AND transaction.criticalStatus = :criticalStatus", Transaction.class)
					.setParameter("approvalStatus", status)
					.setParameter("criticalStatus", false)
					.getResultList();
		}
		catch(NoResultException e) {
			// returns null if no transaction is found
			return null;
		}
	}
}
