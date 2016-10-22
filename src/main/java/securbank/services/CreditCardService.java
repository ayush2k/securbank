package securbank.services;

import java.time.LocalDateTime;
import java.util.UUID;

import securbank.models.CreditCard;
import securbank.models.CreditCardStatement;
import securbank.models.Transaction;
import securbank.models.User;

public interface CreditCardService {
	/**
	 * Creates a new credit card if there is not already
	 * @param user
	 *            The user for which to create a credit card
	 *
	 * @return The newly created credit card.
	 */
	public CreditCard createCreditCard(User user);

//	/**
//	 * Generates the daily interest using the following formula:
//	 *
//	 * Interest = Average Daily Balance * APR / 365.
//	 *
//	 * The Average Daily Balance is determined first by multiplying each balance
//	 * by the number of days you carried it in this billing period, then
//	 * dividing by the number of days in the billing period.
//	 *
//	 * @param creditCard
//	 *            The card to generate the interest from.
//	 * @param startBillingPeriodDt
//	 *            The beginning of the billing period.
//	 * @param endBillingPeriodDt
//	 *            The end of the billing period.
//	 * @return The generated interest.
//	 */
//	public double generateInterest(CreditCard creditCard, LocalDateTime startBillingPeriodDt, LocalDateTime endBillingPeriodDt);

	/**
	 * Retrieves the details of the credit card under the given user.
	 *
	 * @param user
	 *            The user for which to search for
	 * @return The credit card details.
	 */
	public CreditCard getCreditCardDetails(User user);
	
	/**
	 * Creates a credit card transaction
	 *
	 * @param transaction
	 *            The transaction to be created
	 * @param cc
	 *            The credit card to add transaction
	 * @return The transaction details.
	 */
	public Transaction createCreditCardTransaction(Transaction transaction, CreditCard cc);
	

	/**
	 * Creates a credit card transaction
	 *
	 * @param transaction
	 *            The transaction to be created
	 * @param cc
	 *            The credit card to add transaction
	 * @return The transaction details.
	 */
	public Transaction creditCardMakePayment(Transaction transaction, CreditCard cc);

	/**
	 * Gets statement by Id
	 *
	 * @param cc
	 *            The current user cc
	 * @param statementId
	 *            The id of the statement
	 * @return The statement details.
	 */
	public CreditCardStatement getStatementById(CreditCard cc, UUID statementId);
}
