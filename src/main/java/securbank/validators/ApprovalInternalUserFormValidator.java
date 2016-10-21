package securbank.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

import securbank.dao.ModificationRequestDao;
import securbank.dao.UserDao;
import securbank.models.ModificationRequest;
import securbank.models.User;
import securbank.utils.ContraintUtils;
/**
 * @author Ayush Gupta
 *
 */
@Component("approvalInternalUserFormValidator")
public class ApprovalInternalUserFormValidator implements Validator{

	@Autowired
	private UserDao userDao;

	@Autowired
	private ModificationRequestDao modificationRequestDao;

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
		ModificationRequest request = (ModificationRequest) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "request.firstName.required", "First Name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "request.lastName.required", "Last Name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "request.email.required", "Email is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "request.phone.required", "Phone Number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressLine1", "request.addressLine1.required", "Address is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "request.city.required", "City is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "zip", "request.zip.required", "Zip is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state", "request.phone.required", "State is required");
		ModificationRequest current = modificationRequestDao.findById(request.getModificationRequestId());
		if (current == null) {
			errors.reject("request.global", "Request invalid");
		}
		User user = current.getUser();
		
		if (!errors.hasFieldErrors("email")) {
			if (!ContraintUtils.validateEmail(request.getEmail())) {
				errors.rejectValue("email", "request.email.contraint", "Invalid Email");
			}
			else if (!request.getEmail().equals(user.getEmail()) && userDao.emailExists(request.getEmail())) {
				errors.rejectValue("email", "request.email.exists", "Email exists");
			}
		}
		
		if (!errors.hasFieldErrors("phone")) {
			if (!ContraintUtils.validatePhone(request.getPhone())) {
				errors.rejectValue("phone", "request.phone.contraint", "Invalid Phone");
			}
			else if (!request.getPhone().equals(user.getPhone()) && userDao.phoneExists(request.getPhone())) {
				errors.rejectValue("phone", "request.phone.exists", "Phone number exists");
			}
		}
		
		if (!errors.hasFieldErrors("zip") && !ContraintUtils.validateZip(request.getZip())) {
			errors.rejectValue("zip", "request.zip.invalid", "Invalid Zip");
		}

		if (!errors.hasFieldErrors("role") && !ContraintUtils.validateInternalRole(request.getRole())) {
			errors.rejectValue("role", "request.role.invalid", "Invalid Role");
		}
	} 
}
