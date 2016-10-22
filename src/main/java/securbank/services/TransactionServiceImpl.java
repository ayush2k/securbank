package securbank.services;

import org.joda.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import securbank.dao.TransactionDao;
import securbank.models.Account;
import securbank.models.Transaction;

@Service("transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	private TransactionDao transactionDao;

	@Override
	public List<Transaction> getTransactionsByAccountNumberAndDateTimeRange(Account account, LocalDateTime start, LocalDateTime end) {
		return transactionDao.findByAccountNumberAndDateRange(account, start, end);
	}

}
