package securbank.services;

import java.time.Duration;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import securbank.dao.CreditCardDao;
import securbank.models.Account;
import securbank.models.CreditCard;
import securbank.models.Transaction;
import securbank.models.User;

@Service("creditCardService")
@Transactional
public class CreditCardServiceImpl implements CreditCardService {
	public static final int SECONDS_PER_YEAR = 31536000;
	public static final int TRANSACTION_PAGE_SIZE = 100;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private CreditCardDao creditCardDao;
	
	@Autowired
	Environment env;
	
	@Override
	public CreditCard createCreditCard(User user) {
		Account account = new Account();
		account.setBalance(Double.parseDouble(env.getProperty("credit-card.limit")));
		account.setCreatedOn(org.joda.time.LocalDateTime.now());
		account.setType("card");
		account.setUser(user);
		account.setActive(true);
		
		CreditCard cc = new CreditCard();
		cc.setAccount(account);
		cc.setApr(Double.parseDouble(env.getProperty("credit-card.apr")));
		cc.setMaxLimit(Double.parseDouble(env.getProperty("credit-card.limit")));
		cc.setActive(true);
	
		return creditCardDao.save(cc);
	}

	@Override
	public double generateInterest(CreditCard cc, LocalDateTime startBillingPeriodDt, LocalDateTime endBillingPeriodDt) {
//		if (!startBillingPeriodDt.isBefore(endBillingPeriodDt)) {
//			// The end of the billing period must occur after the start.
//			return 0d;
//		}
//		long billingPeriodDurationSeconds = Duration.between(startBillingPeriodDt, endBillingPeriodDt).getSeconds();
//		int offset = 0;
//		LocalDateTime startIntervalDt = startBillingPeriodDt;
//		List<Transaction> transactions;
//		Transaction lastTransaction = null;
//		double avgBalance = 0d;
//		do {
//			transactions = transactionService.getTransactionsByAccountNumberAndDateTimeRange(cc.getAccount(), startBillingPeriodDt,
//					endBillingPeriodDt, TRANSACTION_PAGE_SIZE, offset);
//			for (Transaction transaction : transactions) {
//				LocalDateTime endIntervalDt = transaction.getCreatedOn();
//				long intervalSeconds = Duration.between(startIntervalDt, endIntervalDt).getSeconds();
//				avgBalance += transaction.getOldBalance() * intervalSeconds / billingPeriodDurationSeconds;
//				startIntervalDt = endIntervalDt;
//			}
//			if (!transactions.isEmpty()) {
//				lastTransaction = transactions.get(transactions.size() - 1);
//			}
//			offset += TRANSACTION_PAGE_SIZE;
//		} while (transactions.size() >= TRANSACTION_PAGE_SIZE);
//		double lastBalance = getLastBalanceBeforeDateTime(cc, endBillingPeriodDt, lastTransaction);
//		long intervalSeconds2 = Duration.between(startIntervalDt, endBillingPeriodDt).getSeconds();
//		avgBalance += lastBalance * intervalSeconds2 / billingPeriodDurationSeconds;
//		return avgBalance * cc.getApr() * billingPeriodDurationSeconds / SECONDS_PER_YEAR;
		
		return 0.00;
	}

	/**
	 * @param cc
	 *            The credit card to use.
	 * @param endDt
	 *            The end datetime.
	 * @param lastTransaction
	 *            If not null, assume this is the last transaction to occur
	 *            before the end datetime (to save from hitting the database
	 *            again), unless this transaction's createdOn datetime is later
	 *            than endDt.
	 * @return The final balance at the end datetime.
	 */
	private double getLastBalanceBeforeDateTime(CreditCard cc, LocalDateTime endDt, Transaction lastTransaction) {
//		Transaction lastTransaction2;
//		if (lastTransaction == null || !lastTransaction.getCreatedOn().isBefore(endDt)) {
//			lastTransaction2 = transactionService.getLastTransactionByAccountNumberBeforeDateTime(
//					cc.getAccountNumber(), endDt);
//		} else {
//			lastTransaction2 = lastTransaction;
//		}
//		if (lastTransaction2 == null) {
//			return 0d;
//		}
//		return lastTransaction2.getNewBalance();
		return 0d;
	}

	@Override
	public CreditCard getCreditCardDetails(User user) {
		return creditCardDao.findByUser(user);
	}
}
