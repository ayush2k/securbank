/**
 * 
 */
package securbank.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import securbank.models.Transaction;
import securbank.utils.ContraintUtils;

/**
 * @author Mitikaa
 *
 */

@Component("newTransactionFormValidator")
public class NewTransactionFormValidator implements Validator{

	/**
     * If supports class
     * 
     * @param clazz
     *            The class to check
     *            
     * @return boolean
     */	
	@Override
	public boolean supports(Class<?> clazz) {
		return Transaction.class.equals(clazz);
	}

	/**
     * Validates initiate transaction form
     * 
     * @param target
     *            The target object
     * @param errors
     *            The errors object
     */
	@Override
	public void validate(Object target, Errors errors) {
		Transaction transaction = (Transaction) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "transaction.type.required", "Transaction type is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", "transaction.amount.required", "Transaction amount is required");
		
		if (!errors.hasFieldErrors("type") && !ContraintUtils.validateTransactionType(transaction.getType())) {
				errors.rejectValue("type", "transaction.type.invalid", "Invalid Transaction Type");
		}
		
		if (!errors.hasFieldErrors("amount") && !ContraintUtils.validateTransactionAmount(Double.toString(transaction.getAmount()))) {
			errors.rejectValue("amount", "transaction.amount.invalid", "Invalid Amount");
		}
		
	}

}
