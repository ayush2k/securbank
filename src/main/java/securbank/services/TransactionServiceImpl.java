package securbank.services;

import java.util.List;
import java.util.Set;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import securbank.dao.TransactionDao;
import securbank.models.Account;
import securbank.models.Transaction;
import securbank.models.User;

@Service("transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	private TransactionDao transactionDao;

	@Autowired
 	private EmailService emailService;
 	
	@Autowired 
	private UserService userService;
	
 	@Autowired
 	private Environment env;
 	
 	private SimpleMailMessage message;
 	
 	final static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
	@Override
	public List<Transaction> getTransactionsByAccountAndDateTimeRange(Account account, LocalDateTime start, LocalDateTime end) {
		return transactionDao.findByAccountAndDateRange(account, start, end);
	}
	
	@Override
	public Transaction initiateCredit(Transaction transaction) {
		logger.info("Initiating new credit request");
		
		User currentUser = userService.getCurrentUser();
		if(currentUser==null){
			logger.info("Current logged in user is null");
			return null;
		}
		
		//get user's checking account 
		logger.info("Getting current user's checking account");
		for (Account acc: userService.getCurrentUser().getAccounts()){
			if (acc.getType().equalsIgnoreCase("checking")){
				transaction.setAccount(acc);
			}
		}
		transaction.setApprovalStatus("Pending");
		if (transaction.getAmount() > Double.parseDouble(env.getProperty("critical.amount"))) {
			transaction.setCriticalStatus(true);
		}

		transaction.setType("CREDIT");
		transaction.setCreatedOn(LocalDateTime.now());
		transaction.setActive(true);
		transaction = transactionDao.save(transaction);
		logger.info("After TransactionDao save");

		//send email to user
		User user = transaction.getAccount().getUser();
		message = new SimpleMailMessage();
		message.setText(env.getProperty("external.user.transaction.credit.body"));
		message.setSubject(env.getProperty("external.user.transaction.subject"));
		message.setTo(user.getEmail());
		emailService.sendEmail(message);
		
		return transaction;
	}

	@Override
	public Transaction initiateCreditCardTransaction(Transaction transaction) {
		logger.info("Initiating new credit request");
		
		transaction.setApprovalStatus("Pending");
		if (transaction.getAmount() > Double.parseDouble(env.getProperty("critical.amount"))) {
			transaction.setCriticalStatus(true);
		}

		transaction.setType("CREDIT");
		transaction.setCreatedOn(LocalDateTime.now());
		transaction.setActive(true);
		transaction = transactionDao.save(transaction);
		logger.info("After TransactionDao save");

		//send email to user
		User user = transaction.getAccount().getUser();
		message = new SimpleMailMessage();
		message.setText(env.getProperty("external.user.transaction.credit.body"));
		message.setSubject(env.getProperty("external.user.transaction.subject"));
		message.setTo(user.getEmail());
		emailService.sendEmail(message);
		
		return transaction;
	}

	public List<Transaction> getTransactionsByAccount(Account account) {
		return transactionDao.findByAccount(account);
	}
	
	public Double getSumByAccountAndDateRange(Account account, LocalDateTime start, LocalDateTime end) {
		return transactionDao.findSumByAccountAndDateRange(account, start, end);
	}
	
	public Transaction createInternalTransationByType(Transaction transaction, String type) {
		logger.info("Initiating new fees");
 		
 		transaction.setApprovalStatus("Approved");
 		transaction.setType(type);
 		transaction.setCreatedOn(LocalDateTime.now());
 		transaction.setActive(true);
 		transaction = transactionDao.save(transaction);
 		logger.info("After TransactionDao save");
 
 		//send email to user
 		User user = transaction.getAccount().getUser();
 		message = new SimpleMailMessage();
 		message.setText(env.getProperty("external.user.transaction.interest.body"));
 		message.setSubject(env.getProperty("external.user.transaction.subject"));
 		message.setTo(user.getEmail());
 		emailService.sendEmail(message);
 		
 		return transaction;
	}
	
	public Transaction createCardPaymentTransaction(Double amount, User user) {
		Set<Account> accounts = user.getAccounts();
		Account account = null;
		for (Account acc : accounts) {
			if (acc.getType().equalsIgnoreCase("checking")) {
				account = acc;
			}
		}
		
		if (account == null) {
			return null;
		}
		
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		Double pendingAmount = 0.0;
		
		//in Transaction model
		for(Transaction trans: transactionDao.findPendingByAccountAndType(account, "CREDIT")){
			pendingAmount += trans.getAmount();
		}	
		
//		//check for pending transfer amounts
//		for(Transfer transf: transferDao.findTransferByFromAccount(account)){
//			pendingAmount += transf.getAmount();
//		}
		
		if(pendingAmount + transaction.getAmount() > transaction.getAccount().getBalance()){
			return null;
		}
		
		transaction.setApprovalStatus("Approved");
		if (transaction.getAmount() > Double.parseDouble(env.getProperty("critical.amount"))) {
			transaction.setCriticalStatus(true);
		}

		transaction.setType("DEBIT");
		transaction.setCreatedOn(LocalDateTime.now());
		transaction.setActive(true);
		transaction = transactionDao.save(transaction);

		return transaction;
	}
}
