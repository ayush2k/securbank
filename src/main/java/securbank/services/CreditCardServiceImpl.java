package securbank.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import securbank.dao.AccountDao;
import securbank.dao.CreditCardDao;
import securbank.dao.CreditCardStatementDao;
import securbank.models.Account;
import securbank.models.CreditCard;
import securbank.models.CreditCardStatement;
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
	private AccountDao accountDao;
	
	@Autowired
	private CreditCardStatementDao creditCardStatementDao; 
	
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
		
		// Creates new statement for credit card
		CreditCardStatement statement = new CreditCardStatement();
		CreditCard cc = new CreditCard();
		cc.setAccount(account);
		cc.setApr(Double.parseDouble(env.getProperty("credit-card.apr")));
		cc.setMaxLimit(Double.parseDouble(env.getProperty("credit-card.limit")));
		cc.setActive(true);
		statement.setCc(cc);
		statement = creditCardStatementDao.save(statement);
		
		return statement.getCc();
	}

	@Override
	public CreditCard getCreditCardDetails(User user) {
		return creditCardDao.findByUser(user);
	}
	
	public Transaction createCreditCardTransaction(Transaction transaction, CreditCard cc) {
		transaction.setAccount(cc.getAccount());
		transaction = transactionService.initiateCreditCardTransaction(transaction);
		
		return transaction;
	}
	
	public Transaction creditCardMakePayment(CreditCard cc) {
		List<CreditCardStatement> statements = creditCardStatementDao.findByCreditCardAndStatus(cc, "pending");
		if (statements.size() == 0) {
			return new Transaction();
		}
		for(CreditCardStatement statement : statements) {
			statement.setStatus("closed");
			creditCardStatementDao.update(statement);
		}
		
		Double balance = cc.getMaxLimit() - cc.getAccount().getBalance();
		
		Transaction transaction = transactionService.createCardPaymentTransaction(balance, cc.getAccount().getUser());
		if (transaction == null) {
			return null;
		}
		Account account = cc.getAccount();
		account.setBalance(cc.getMaxLimit());
		accountDao.update(account);
		

		return transaction;
	}
	
	public CreditCard getDueAmount(CreditCard cc) {
		List<CreditCardStatement> statements = creditCardStatementDao.findByCreditCardAndStatus(cc, "pending");
		if (statements.size() == 0) {
			cc.setBalance(0d);
		}
		else {
			cc.setBalance(cc.getMaxLimit() - cc.getAccount().getBalance());
		}
		
		return cc;
	}
	
	public CreditCardStatement getStatementById(CreditCard cc, UUID statementId) {
		CreditCardStatement statement = creditCardStatementDao.findById(statementId);
		if (statement == null) {
			return null;
		}
		List<Transaction> transactions = transactionService.getTransactionsByAccountAndDateTimeRange(cc.getAccount(), statement.getStartDate().toLocalDateTime(LocalTime.fromMillisOfDay(0)), statement.getEndDate().toLocalDateTime(LocalTime.fromMillisOfDay(0)));
		if (transactions == null) {
			return statement;
		}
		statement.setTransactions(transactions);
		
		return statement;
	}
	
	/*
	 * Calls this function at 1 AM daily
	 */
	//@Scheduled(cron = "0 00 1 * * *")
	@Scheduled(fixedDelay = 5000)
	public void interestGeneration() {
		List<CreditCardStatement> statements = creditCardStatementDao.findByPendingDateAndStatus(LocalDate.now(), "pending");
		Map<CreditCard, Double> creditCards = new HashMap<CreditCard, Double>();
		Double pendingBalance = 0d;
		Account account = null;
		Double apr = 0d;
		
		for (CreditCardStatement statement : statements) {
			if (!creditCards.containsKey(statement.getCc())) {
				creditCards.put(statement.getCc(), statement.getClosingBalance());
			}
			else {
				creditCards.put(statement.getCc(), creditCards.get(statement.getCc()) + statement.getClosingBalance());
			}
		}
		for (CreditCard cc : creditCards.keySet()) {
			account = cc.getAccount();
			pendingBalance = creditCards.get(cc);
			apr = cc.getApr();
			Transaction transaction = new Transaction();
			transaction.setAccount(account); 
			transaction.setAmount(Math.round(pendingBalance * (apr / (30 * 100))*100)/100d);
			transactionService.createInternalTransationByType(transaction, "APR");
		}
	}
	
	/*
	 * Calls this function at 2 AM daily
	 */
	@Scheduled(cron = "0 00 2 * * *")
	public void latefeesGeneration() {
		List<CreditCardStatement> statements = creditCardStatementDao.findByGenerationDateAndStatus(LocalDate.now().withYear(2000), "pending");
		Map<CreditCard, Integer> ccMap = new HashMap<CreditCard, Integer>();
		Account account = null;
		
		for (CreditCardStatement statement : statements) {
			if (!ccMap.containsKey(statement.getCc())) {
				ccMap.put(statement.getCc(), 1);
			}
			else {
				ccMap.put(statement.getCc(), ccMap.get(statement.getCc()) + 1);
			}
		}
		for (CreditCard cc : ccMap.keySet()) {
			account = cc.getAccount();
			for (int i = 0; i< ccMap.get(cc); i++) {
				Transaction transaction = new Transaction();
				transaction.setAccount(account); 
				transaction.setAmount(Integer.parseInt(env.getProperty("credit-card.latefee")));
				transactionService.createInternalTransationByType(transaction, "LATE");
			}
		}
	}
	
	/*
	 * Calls this function at 3 AM daily
	 */
	@Scheduled(cron = "0 00 3 * * *")
	public void statementGeneration() {
		List<CreditCard> ccs = creditCardDao.findByGenerationDate(LocalDate.now().withYear(2000));
		CreditCardStatement statement = null;
		
		for (CreditCard cc : ccs) {
			for (CreditCardStatement stat : cc.getStatements()) {
				if (stat.getStatus().equals("current")) {
					Double balance = transactionService.getSumByAccountAndDateRange(cc.getAccount(), 
							stat.getStartDate().toLocalDateTime(LocalTime.fromMillisOfDay(0)), 
							stat.getEndDate().toLocalDateTime(LocalTime.fromMillisOfDay(0)));
					stat.setClosingBalance(balance);
					stat.setStatus("pending");
					creditCardStatementDao.update(stat);
				}
			}
			statement = new CreditCardStatement();
			statement.setCc(cc);
			creditCardStatementDao.save(statement);
		}
	}
}
