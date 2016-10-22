package securbank.dao;

import securbank.models.Account;
import securbank.models.Transaction;

import org.joda.time.LocalDateTime;
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
	public List<Transaction> findByAccountNumberAndDateRange(Account account, LocalDateTime start, LocalDateTime end);
	public Transaction findByStatus(Boolean criticalStatus);
}
