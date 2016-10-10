package securbank.services;

import java.time.LocalDateTime;
import java.util.List;

import securbank.models.Transaction;

public interface TransactionService {
	/**
	 * @param accountNumber
	 *            The account number to get the last transaction from.
	 * @param endDt
	 *            The last transaction occurs before this datetime (exclusive).
	 * @return The last transaction.
	 */
	public Transaction getLastTransactionByAccountNumberBeforeDateTime(long accountNumber, LocalDateTime endDt);

	/**
	 * This function allows us to use pagination when retrieving transactions.
	 *
	 * @param accountNumber
	 *            The account number to get the transactions from.
	 * @param startDt
	 *            The start datetime (inclusive)
	 * @param endDt
	 *            The end datetime (exclusive)
	 * @param limit
	 *            The maximum number of transactions to retrieve at a time.
	 * @param offset
	 *            The offset to start from when retrieving the transactions.
	 * @return A list of transactions belonging to the given account number
	 *         within the given datetime range.
	 */
	public List<Transaction> getTransactionsByAccountNumberAndDateTimeRange(long accountNumber, LocalDateTime startDt,
			LocalDateTime endDt, int limit, int offset);
}
