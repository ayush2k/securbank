package securbank.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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
	public Transaction findByAccount(String accountNumber) {
		try {
			return (Transaction) this.entityManager.createQuery("SELECT transaction from Transaction transaction"
					+ " where transaction.accountnumber = :accountNumber")
					.setParameter("accountNumber", accountNumber)
					.getSingleResult();
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
	public Transaction findByAccountAndType(String accountNumber, String type) {
		try {
			return (Transaction) this.entityManager.createQuery("SELECT transaction from Transaction transaction"
					+ " where (transaction.accountnumber = :accountNumber AND transaction.type = type)")
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
	@Override
	public Transaction findByStatus(Boolean criticalStatus) {
		try {
			return (Transaction) this.entityManager.createQuery("SELECT transaction from Transaction transaction"
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
	public List<Transaction> findByAccountNumberAndDateRange(Account account, LocalDateTime start, LocalDateTime end) {
		return this.entityManager.createQuery("SELECT transaction FROM Transaction transaction"
				+ " WHERE transaction.account = :account"
				+ " AND transaction.created_on >= :start"
				+ " AND transaction.created_on < :end"
				+ " AND transaction.active < true"
				+ " ORDER BY DESC transaction.created_on", Transaction.class)
				.setParameter("account", account)
				.setParameter("start", start)
				.setParameter("end", end)
				.getResultList();
	}
}
