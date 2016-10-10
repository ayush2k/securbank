package securbank.services;

import java.time.LocalDateTime;

import securbank.models.Account;
import securbank.models.CreditCard;

public interface CreditCardService {
	/**
	 * Creates a new credit card if there is not already
	 *
	 * @param accountId
	 *            The id of the account the credit card will be under.
	 * @param apr
	 *            The annual percentage rate (APR) of the credit card.
	 * @param maxLimit
	 *            The maximum balance the issuer allows on the credit card.
	 * @return The newly created credit card.
	 */
	public CreditCard createCreditCard(Account account, double apr, double maxLimit);

	/**
	 * Generates the daily interest using the following formula:
	 *
	 * Interest = Average Daily Balance * APR / 365.
	 *
	 * The Average Daily Balance is determined first by multiplying each balance
	 * by the number of days you carried it in this billing period, then
	 * dividing by the number of days in the billing period.
	 *
	 * @param creditCard
	 *            The card to generate the interest from.
	 * @param startBillingPeriodDt
	 *            The beginning of the billing period.
	 * @param endBillingPeriodDt
	 *            The end of the billing period.
	 * @return The generated interest.
	 */
	public double generateInterest(CreditCard creditCard, LocalDateTime startBillingPeriodDt, LocalDateTime endBillingPeriodDt);

	/**
	 * Retrieves the details of the credit card under the given account.
	 *
	 * @param accountId
	 *            The id of the account the credit card is under.
	 * @return The credit card details.
	 */
	public CreditCard getCreditCardDetails(Account account);
}
