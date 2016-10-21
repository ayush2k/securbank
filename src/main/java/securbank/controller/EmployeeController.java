package securbank.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import securbank.models.ModificationRequest;
import securbank.models.User;
import securbank.services.UserService;
import securbank.validators.ApprovalUserFormValidator;
import securbank.validators.InternalEditUserFormValidator;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class EmployeeController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private InternalEditUserFormValidator editUserFormValidator;

	@Autowired
	private ApprovalUserFormValidator approvalUserFormValidator;

	final static Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@GetMapping("/employee/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=404&path=user-notfound";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: Employee user detail");
		
        return "employee/detail";
    }
	
	@GetMapping("/employee/edit")
    public String editUser(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error";
		}
		model.addAttribute("user", user);
		logger.info("GET request: Employee profile edit");
		
        return "employee/edit";
    }
	
	@PostMapping("/employee/edit")
    public String editSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		editUserFormValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			return "employee/edit";
        }
		
		// create request
    	userService.createInternalModificationRequest(user);
    	logger.info("POST request: Employee New modification request");
    	
        return "redirect:/";
    }
	
	@GetMapping("/employee/user/request")
    public String getAllUserRequest(Model model) {
		List<ModificationRequest> modificationRequests = userService.getModificationRequests("pending", "external");
		if (modificationRequests == null) {
			model.addAttribute("modificationrequests", new ArrayList<ModificationRequest>());
		}
		else {
			model.addAttribute("modificationrequests", modificationRequests);	
		}
		logger.info("GET request: Employee All external modification requests");
		
        return "employee/modificationrequests";
    }
	
	@GetMapping("/employee/user/request/view/{id}")
    public String getUserRequest(Model model, @PathVariable() UUID id) {
		ModificationRequest modificationRequest = userService.getModificationRequest(id);
		
		if (modificationRequest == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		
		model.addAttribute("modificationrequest", modificationRequest);
		logger.info("GET request: Employee external modification request by ID");
		
        return "employee/modificationrequest_detail";
    }
	
	@PostMapping("/employee/user/request/{requestId}")
    public String approveEdit(@PathVariable UUID requestId, @ModelAttribute ModificationRequest request, BindingResult bindingResult) {
		String status = request.getStatus();
		if (status == null || !(request.getStatus().equals("approved") || request.getStatus().equals("rejected"))) {
			return "redirect:/error?code=400&path=request-action-invalid";
		}
		
		// checks validity of request
		if (userService.getModificationRequest(requestId) == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		request.setModificationRequestId(requestId);
		approvalUserFormValidator.validate(request, bindingResult);
		if (bindingResult.hasErrors()) {
			return "redirect:/error?code=400&path=request-action-validation";
		}
		
		// checks if employee is authorized for the request to approve
		if (!userService.verifyModificationRequestUserType(requestId, "external")) {
			logger.warn("GET request: Admin unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		else {
			request.setUserType("external");
		}
		
		request.setStatus(status);
		if (status.equals("approved")) {
			userService.approveModificationRequest(request);
		}
		// rejects request
		else {
			userService.rejectModificationRequest(request);
		}
		logger.info("POST request: Employee approves external modification request");
		
        return "redirect:/employee/user/request";
    }
	
	@GetMapping("/employee/user/request/delete/{id}")
    public String deleteRequest(Model model, @PathVariable() UUID id) {
		ModificationRequest modificationRequest = userService.getModificationRequest(id);
		
		if (modificationRequest == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		
		model.addAttribute("modificationrequest", modificationRequest);
		logger.info("GET request: Employee external modification request by ID");
		
        return "employee/modificationrequest_delete";
    }
	
	@PostMapping("/employee/user/request/delete/{requestId}")
    public String deleteRequest(@PathVariable UUID requestId, @ModelAttribute ModificationRequest request, BindingResult bindingResult) {
		request = userService.getModificationRequest(requestId);
		
		// checks validity of request
		if (request == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		
		// checks if admin is authorized for the request to approve
		if (!request.getUserType().equals("external")) {
			logger.warn("GET request: Employee unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		userService.deleteModificationRequest(request);
		logger.info("POST request: Employee approves modification request");
		
        return "redirect:/employee/user/request";
    }	
}
