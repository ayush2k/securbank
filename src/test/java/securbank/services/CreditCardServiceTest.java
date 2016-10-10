package securbank.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import securbank.dao.CreditCardDao;
import securbank.models.Account;
import securbank.models.CreditCard;
import securbank.models.Transaction;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditCardServiceTest {
	@Mock
	private CreditCardDao creditCardDao;

	@Mock
	private TransactionService transactionService;

	@InjectMocks
	private CreditCardServiceImpl creditCardService;

	private Account testAccount;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void createCreditCard_savesIfAccountHasNoActiveCreditCard() {
		double apr = 0.15d;
		double maxLimit = 5000d;
		Account account = new Account();
		account.setAccountNumber(getTestAccountNumber());
		Mockito.when(creditCardDao.findByAccountNumber(getTestAccountNumber())).thenReturn(null);
		creditCardService.createCreditCard(account, apr, maxLimit);
		Mockito.verify(creditCardDao).save(Mockito.any());
	}

	@Test
	public void createCreditCard_doesNothingIfAccountHasActiveCreditCard() {
		double apr = 0.15d;
		double maxLimit = 5000d;
		Account account = new Account();
		account.setAccountNumber(getTestAccountNumber());
		Mockito.when(creditCardDao.findByAccountNumber(getTestAccountNumber())).thenReturn(new CreditCard());
		creditCardService.createCreditCard(account, apr, maxLimit);
		Mockito.verify(creditCardDao, Mockito.never()).save(Mockito.any());
	}

	@Test
	public void generateInterest_noTransactionsDuringBillingPeriod() {
		// Neither the old balance of the last transaction before the billing
		// period, nor the balance that occurred after the billing period,
		// should be considered in calculating the interest rate. The final
		// balance before the billing period does have an effect.
		// - No effect on interest rate.
		double testVeryOldBalance = 730d;
		// - Affects interest rate.
		double testFinalBalanceBeforeBillingPeriod = 1460d;
		// - No effect on interest rate.
		double testBalanceSometimeAfterBillingPeriod = 2920d;

		CreditCard cc = getTestCreditCard();
		cc.setBalance(testBalanceSometimeAfterBillingPeriod);
		Transaction t = new Transaction();
		t.setAccount(getTestAccount());
		t.setOldBalance(testVeryOldBalance);
		t.setNewBalance(testFinalBalanceBeforeBillingPeriod);
		Mockito.when(transactionService.getTransactionsByAccountNumberAndDateTimeRange(getTestAccountNumber(),
				getTestStartBillingPeriodDateTime(), getTestEndBillingPeriodDateTime(), 100, 0))
				.thenReturn(new ArrayList<Transaction>());
		Mockito.when(transactionService.getLastTransactionByAccountNumberBeforeDateTime(getTestAccountNumber(),
				getTestEndBillingPeriodDateTime())).thenReturn(t);
		double expected = testFinalBalanceBeforeBillingPeriod * getTestApr() * getTestBillingPeriodDurationSeconds()
				/ CreditCardServiceImpl.SECONDS_PER_YEAR;
		double actual = creditCardService.generateInterest(cc, getTestStartBillingPeriodDateTime(),
				getTestEndBillingPeriodDateTime());
		Assert.assertEquals(expected, actual, 0.01d);
	}

	@Test
	public void generateInterest_transactionsDuringBillingPeriod() {
		int[] numTransactionTests = { 0, 1, 2, 99, 100, 101, 300 };

		for (int i = 0; i < numTransactionTests.length; i++) {
			int numTransactions = numTransactionTests[i];
			List<Transaction> transactions = generateInterest_transactionsDuringBillingPeriod_generateTransactions(
					numTransactions);
			for (int offset = 0; offset < numTransactions; offset += CreditCardServiceImpl.TRANSACTION_PAGE_SIZE) {
				Mockito.when(transactionService.getTransactionsByAccountNumberAndDateTimeRange(getTestAccountNumber(),
						getTestStartBillingPeriodDateTime(), getTestEndBillingPeriodDateTime(),
						CreditCardServiceImpl.TRANSACTION_PAGE_SIZE, offset))
						.thenReturn(transactions.subList(offset,
								Math.min(offset + CreditCardServiceImpl.TRANSACTION_PAGE_SIZE, numTransactions)));
			}
			Mockito.when(transactionService.getLastTransactionByAccountNumberBeforeDateTime(getTestAccountNumber(),
					getTestEndBillingPeriodDateTime()))
					.thenReturn(numTransactions > 0 ? transactions.get(numTransactions - 1) : null);
			double expected = generateInterest_transactonsDuringBillingPeriod_generateExpected(numTransactions);
			double actual = creditCardService.generateInterest(getTestCreditCard(), getTestStartBillingPeriodDateTime(),
					getTestEndBillingPeriodDateTime());
			Assert.assertEquals(expected, actual, 0.01d);
		}
	}

	private double generateInterest_transactonsDuringBillingPeriod_generateExpected(int numTransactions) {
		if (numTransactions < 1) {
			return 0d;
		}
		// Here, it is assumed that each transaction adds 1 dollar to the
		// balance and that each transaction is evenly spaced apart.
		long secondsBetweenTransactions = getTestBillingPeriodDurationSeconds() / numTransactions;
		long remainderSeconds = getTestBillingPeriodDurationSeconds() % numTransactions;
		long squareUnits = secondsBetweenTransactions * numTransactions * (numTransactions + 1) / 2
				+ numTransactions * remainderSeconds;
		return squareUnits * getTestApr() / CreditCardServiceImpl.SECONDS_PER_YEAR;
	}

	private List<Transaction> generateInterest_transactionsDuringBillingPeriod_generateTransactions(
			int numTransactions) {
		// For simplicity, each test transaction adds 1 dollar to the balance.
		List<Transaction> tList = new ArrayList<Transaction>();
		if (numTransactions < 1) {
			return tList;
		}
		long secondsBetweenTransactions = getTestBillingPeriodDurationSeconds() / numTransactions;
		LocalDateTime currentDt = getTestStartBillingPeriodDateTime();

		for (int i = 0; i < numTransactions; i++) {
			Transaction t = new Transaction();
			t.setAccount(getTestAccount());
			t.setOldBalance((double) i);
			t.setNewBalance((double) (i + 1));
			t.setCreatedOn(currentDt);
			tList.add(t);
			currentDt = currentDt.plusSeconds(secondsBetweenTransactions);
		}
		return tList;
	}

	@Test
	public void getCreditCardDetails_returnsCardIfActiveCardOnAccount() {
		CreditCard cc1 = getTestCreditCard();
		Mockito.when(creditCardDao.findByAccountNumber(getTestAccountNumber())).thenReturn(cc1);
		CreditCard cc2 = creditCardService.getCreditCardDetails(getTestAccount());
		Assert.assertSame(cc1, cc2);
	}

	@Test
	public void getCreditCardDetails_returnsNullIfNoActiveCardOnAccount() {
		Mockito.when(creditCardDao.findByAccountNumber(getTestAccountNumber())).thenReturn(null);
		CreditCard cc = creditCardService.getCreditCardDetails(getTestAccount());
		Assert.assertNull(cc);
	}

	private Account getTestAccount() {
		if (this.testAccount == null) {
			this.testAccount = new Account();
			this.testAccount.setAccountNumber(5l);
		}
		return this.testAccount;
	}

	private long getTestAccountNumber() {
		return getTestAccount().getAccountNumber();
	}

	private double getTestApr() {
		return 0.10d;
	}

	private long getTestBillingPeriodDurationSeconds() {
		return Duration.between(getTestStartBillingPeriodDateTime(), getTestEndBillingPeriodDateTime()).getSeconds();
	}

	private CreditCard getTestCreditCard() {
		CreditCard cc = new CreditCard();
		cc.setAccount(getTestAccount());
		cc.setApr(getTestApr());
		return cc;
	}

	private LocalDateTime getTestEndBillingPeriodDateTime() {
		return LocalDate.of(2016, 10, 1).atStartOfDay();
	}

	private LocalDateTime getTestStartBillingPeriodDateTime() {
		return LocalDate.of(2016, 9, 1).atStartOfDay();
	}
}
