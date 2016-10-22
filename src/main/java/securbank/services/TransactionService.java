package securbank.services;

import java.util.List;

import org.joda.time.LocalDateTime;

import securbank.models.Account;
import securbank.models.Transaction;
import securbank.models.User;

public interface TransactionService {
		/**
	 * This function allows us to use pagination when retrieving transactions.
	 *
	 * @param accountNumber
	 *            The account number to get the transactions from.
	 * @param start
	 *            The start datetime (inclusive)
	 * @param end
	 *            The end datetime (exclusive)
	 * @return A list of transactions belonging to the given account number
	 *         within the given datetime range.
	 */
	public List<Transaction> getTransactionsByAccountAndDateTimeRange(Account account, LocalDateTime startDt, LocalDateTime endDt);
	public Transaction initiateCredit(Transaction transaction);
	public Transaction initiateCreditCardTransaction(Transaction transaction);
	public List<Transaction> getTransactionsByAccount(Account account);
	public Double getSumByAccountAndDateRange(Account account, LocalDateTime start, LocalDateTime end);
	public Transaction createInternalTransationByType(Transaction transaction, String type);
	public Transaction createCardPaymentTransaction(Double amount, User user);
}
