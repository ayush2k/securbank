package securbank.dao;

import securbank.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Mitikaa Sama
 *
 * Sep 26, 2016
 */

public interface TransactionDao extends BaseDao<Transaction, UUID>{
	public List<Transaction> findAll();
	public Transaction findByAccount(String accountNumber);
	public Transaction findByAccountAndType(String accountNumber, String type);
	public List<Transaction> findByAccountNumberAndDateRange(
			long accountNumber,
			LocalDateTime startDt,
			LocalDateTime endDt,
			int limit,
			int offset);
	public Transaction findByStatus(Boolean criticalStatus);
	public Transaction findLastByAccountNumberBeforeDateTime(long accountNumber, LocalDateTime endDt);
}
