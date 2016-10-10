package securbank.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import securbank.dao.TransactionDao;
import securbank.models.Transaction;

@Service("transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	private TransactionDao transactionDao;

	@Override
	public Transaction getLastTransactionByAccountNumberBeforeDateTime(long accountNumber, LocalDateTime endDt) {
		return transactionDao.findLastByAccountNumberBeforeDateTime(accountNumber, endDt);
	}

	@Override
	public List<Transaction> getTransactionsByAccountNumberAndDateTimeRange(long accountNumber, LocalDateTime start,
			LocalDateTime endDt, int limit, int offset) {
		return transactionDao.findByAccountNumberAndDateRange(accountNumber, start, endDt, limit, offset);
	}

}
