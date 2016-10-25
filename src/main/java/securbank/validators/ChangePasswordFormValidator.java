package securbank.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import securbank.models.ChangePasswordRequest;
import securbank.utils.ContraintUtils;

import org.springframework.validation.ValidationUtils;

/**
 * @author Shivani J
 *
 */
@Component("changePasswordFormValidator")
public class ChangePasswordFormValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return ChangePasswordRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ChangePasswordRequest changePasswordRequest = (ChangePasswordRequest) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "existingPassword", "changePasswordRequest.existingPassword.required", "Existing Password is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "changePasswordRequest.newPassword.required", "New Password is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "changePasswordRequest.confirmPassword.required", "Confirm Password is required");
		
		if (!errors.hasFieldErrors("newPassword")) {
			if (!ContraintUtils.validatePassword(changePasswordRequest.getNewPassword())) {
				errors.rejectValue("newPassword", "changePasswordRequest.newPassword.contraint", "Password should contain one letter, number and special character. Length of password should be between 6 and 20");
			}
			else if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
				errors.rejectValue("newPassword", "changePasswordRequest.newPassword.match", "Password doesn't match");
			}
			else if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getExistingPassword())) {
				errors.rejectValue("newPassword", "changePasswordRequest.newPassword.match", "New Password same as existing password");
			}
		}
	}

}
