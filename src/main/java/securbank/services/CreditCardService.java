package securbank.services;

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
	public Transaction creditCardMakePayment(CreditCard cc);

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
	
	public CreditCard getDueAmount(CreditCard cc);
	
	/**
	 * Runs regular job to apply interest
	 *
	 */
	public void interestGeneration();
	
	/**
	 * Runs regular job to generate new statements
	 *
	 */
	public void statementGeneration();
	
	/**
	 * Runs regular job to apply late fees on unpaid account
	 *
	 */
	public void latefeesGeneration();
	
}
