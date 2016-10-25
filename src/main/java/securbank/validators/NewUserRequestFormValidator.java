package securbank.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

import securbank.models.NewUserRequest;
import securbank.utils.ContraintUtils;
import securbank.dao.NewUserRequestDao;
import securbank.dao.UserDao;
/**
 * @author Ayush Gupta
 *
 */
@Component("newUserRequestFormValidator")
public class NewUserRequestFormValidator implements Validator{

	@Autowired
	private NewUserRequestDao newUserRequestDao;
	
	@Autowired
	private UserDao userDao;
	
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
		return NewUserRequest.class.equals(clazz);
	}

	/**
     * Validates create user form
     * 
     * @param target
     *            The target object
     * @param errors
     *            The errors object
     */
	@Override
	public void validate(Object target, Errors errors) {
		NewUserRequest newUserRequest = (NewUserRequest) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "newuserrequest.email.required", "Email is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", "newuserrequest.role.required", "Role is required");
		
		if (!errors.hasFieldErrors("email")) {
			if (!ContraintUtils.validateEmail(newUserRequest.getEmail())) {
				errors.rejectValue("email", "newuserrequest.email.contraint", "Invalid Email");
			}
			else if (newUserRequestDao.emailExists(newUserRequest.getEmail()) || userDao.emailExists(newUserRequest.getEmail()) ) {
				errors.rejectValue("email", "newuserrequest.email.exists", "Email exists");
			}
		}
		
		if (!errors.hasFieldErrors("role") && !ContraintUtils.validateInternalRole(newUserRequest.getRole())) {
			errors.rejectValue("role", "newuserrequest.role.invalid", "Invalid Role");
		}
	} 
}
