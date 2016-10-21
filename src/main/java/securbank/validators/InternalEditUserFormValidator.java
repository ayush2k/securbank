package securbank.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

import securbank.models.ModificationRequest;
import securbank.models.User;
import securbank.utils.ContraintUtils;
import securbank.dao.UserDao;
/**
 * @author Ayush Gupta
 *
 */
@Component("intenralEditUserFormValidator")
public class InternalEditUserFormValidator implements Validator{

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
		return ModificationRequest.class.equals(clazz);
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
		User user = (User) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "user.username.required", "Username is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "user.firstName.required", "First Name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "user.lastName.required", "Last Name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "user.email.required", "Email is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "user.phone.required", "Phone Number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressLine1", "user.addressLine1.required", "Address is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "user.city.required", "City is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "zip", "user.zip.required", "Zip is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state", "user.phone.required", "State is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", "user.role.required", "Role is required");
		
		User current = userDao.findByUsername(user.getUsername());
		if (current == null) {
			errors.rejectValue("username", "user.username.invalid", "Invalid Username");
		}
		
		if (!errors.hasFieldErrors("email")) {
			if (!ContraintUtils.validateEmail(user.getEmail())) {
				errors.rejectValue("email", "user.email.contraint", "Invalid Email");
			}
			else if (!user.getEmail().equals(current.getEmail()) && userDao.emailExists(user.getEmail())) {
				errors.rejectValue("email", "user.email.exists", "Email exists");
			}
		}
		
		if (!errors.hasFieldErrors("phone")) {
			if (!ContraintUtils.validatePhone(user.getPhone())) {
				errors.rejectValue("phone", "user.phone.contraint", "Invalid Phone");
			}
			else if (!user.getPhone().equals(current.getPhone()) && userDao.phoneExists(user.getPhone())) {
				errors.rejectValue("phone", "user.phone.exists", "Phone number exists");
			}
		}
		
		if (!errors.hasFieldErrors("zip") && !ContraintUtils.validateZip(user.getZip())) {
			errors.rejectValue("zip", "user.zip.invalid", "Invalid Zip");
		}

		if (!errors.hasFieldErrors("role") && !ContraintUtils.validateInternalRole(user.getRole())) {
			errors.rejectValue("role", "user.role.invalid", "Invalid Role");
		}
	} 
}
