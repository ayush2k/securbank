package securbank.dao;

import securbank.models.Transaction;

import java.util.*;

/**
 * @author Mitikaa Sama
 *
 * Sep 26, 2016
 */

public interface TransactionDao extends BaseDao<Transaction, UUID>{
	public List<Transaction> findAll();
	public Transaction findByAccount(String accountNumber);
	public Transaction findByAccountAndType(String accountNumber, String type);
	public Transaction findByStatus(Boolean criticalStatus);
}
