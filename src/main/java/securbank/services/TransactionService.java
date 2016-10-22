package securbank.services;

import org.joda.time.LocalDateTime;
import java.util.List;

import securbank.models.Account;
import securbank.models.Transaction;

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
	public List<Transaction> getTransactionsByAccountNumberAndDateTimeRange(Account account, LocalDateTime startDt,
			LocalDateTime endDt);
}
