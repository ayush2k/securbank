package securbank.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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

	@Override
	@SuppressWarnings("unchecked")
	public List<Transaction> findByAccountNumberAndDateRange(
			long accountNumber,
			LocalDateTime startDt,
			LocalDateTime endDt,
			int limit,
			int offset) {
		return (List<Transaction>) this.entityManager.createQuery("SELECT transaction FROM Transaction transaction"
				+ " WHERE transaction.account_number = :accountNumber"
				+ " AND transaction.created_on >= :startDt"
				+ " AND transaction.created_on < :endDt"
				+ " ORDER BY transaction.created_on, transaction.transaction_id")
				.setParameter("accountNumber", accountNumber)
				.setParameter("startDt", startDt)
				.setParameter("endDt", endDt)
				.setMaxResults(limit)
				.setFirstResult(offset)
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Transaction findLastByAccountNumberBeforeDateTime(long accountNumber, LocalDateTime endDt) {
		List<Transaction> tList = (List<Transaction>) this.entityManager.createQuery(
				"SELECT transaction FROM Transaction transaction"
				+ " WHERE transaction.account_number = :accountNumber"
				+ " AND transaction.created_on < :end"
				+ " ORDER BY transaction.created_on DESC"
				+ " LIMIT 1")
				.setParameter("accountNumber", accountNumber)
				.setParameter("end", endDt)
				.getResultList();
		return tList.isEmpty() ? null : tList.get(0);
	}
}
