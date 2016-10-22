package securbank.dao;

import java.util.List;
import java.util.UUID;

import org.joda.time.LocalDateTime;

import securbank.models.Account;
import securbank.models.Transaction;

/**
 * @author Mitikaa Sama
 *
 * Sep 26, 2016
 */

public interface TransactionDao extends BaseDao<Transaction, UUID>{
	public List<Transaction> findAll();
	public List<Transaction> findByAccount(Account account);
	public List<Transaction> findByAccountAndType(Long accountNumber, String type);
	public List<Transaction> findByCriticalStatus(Boolean criticalStatus);
	public List<Transaction> findByApprovalStatus(String status);
	public List<Transaction> findPendingByAccountAndType(Account account, String type);
	public List<Transaction> findPendingByCriticalStatus(Boolean criticalStatus);
	public List<Transaction> findPendingByAccount(Long accountNumber);
	public Transaction findPendingTransactionByAccount(Long accountNumber);
	public List<Transaction> findByAccountAndDateRange(Account account, LocalDateTime start, LocalDateTime end);
	public Double findSumByAccountAndDateRange(Account account, LocalDateTime start, LocalDateTime end);
}
