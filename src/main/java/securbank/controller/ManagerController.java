/**
 * 
 */
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
public class ManagerController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private InternalEditUserFormValidator editUserFormValidator;

	@Autowired
	private ApprovalUserFormValidator approvalUserFormValidator;

	final static Logger logger = LoggerFactory.getLogger(ManagerController.class);
	
	@GetMapping("/manager/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {

			return "redirect:/error?code=400&path=user-notfound";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: Manager user detail");
			
        return "manager/detail";
    }
	
	@GetMapping("/manager/edit")
    public String editUser(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error";
		}
		model.addAttribute("user", user);
		logger.info("GET request: Manager profile edit");
		
        return "manager/edit";
    }
	
	@PostMapping("/manager/edit")
    public String editSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		editUserFormValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			return "manager/edit";
        }
		
		// create request
    	userService.createInternalModificationRequest(user);
    	logger.info("POST request: Manager New modification request");
    	
        return "redirect:/";
    }
	
	@GetMapping("/manager/user/request")
    public String getAllUserRequest(Model model) {
		List<ModificationRequest> modificationRequests = userService.getModificationRequests("pending", "external");
		if (modificationRequests == null) {
			model.addAttribute("modificationrequests", new ArrayList<ModificationRequest>());
		}
		else {
			model.addAttribute("modificationrequests", modificationRequests);	
		}
		logger.info("GET request: Manager All external modification requests");
		
        return "manager/modificationrequests";
    }
	
	@GetMapping("/manager/user/request/view/{id}")
    public String getUserRequest(Model model, @PathVariable() UUID id) {
		ModificationRequest modificationRequest = userService.getModificationRequest(id);
		
		if (modificationRequest == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}

		// checks if manager is authorized for the request to approve
		if (!modificationRequest.getUserType().equals("external")) {
			logger.warn("GET request: Manager unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
		model.addAttribute("modificationrequest", modificationRequest);
		logger.info("GET request: Manager external modification request by ID");
		
        return "manager/modificationrequest_detail";
    }
	
	@PostMapping("/manager/user/request/{requestId}")
    public String approveEdit(@PathVariable UUID requestId, @ModelAttribute ModificationRequest request, BindingResult bindingResult) {
		String status = request.getStatus();
		if (status == null || !(request.getStatus().equals("approved") || request.getStatus().equals("rejected"))) {
			return "redirect:/error?code=400&path=request-action-invalid";
		}
		
		if (userService.getModificationRequest(requestId) == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}

		request.setModificationRequestId(requestId);
		approvalUserFormValidator.validate(request, bindingResult);
		if (bindingResult.hasErrors()) {
			return "redirect:/error?code=400&path=request-action-validation";
		}
		
		// checks if manager is authorized for the request to approve
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
		
        return "redirect:/manager/user/request";
	}

	@GetMapping("/manager/user")
    public String getUsers(Model model) {
		List<User> users = userService.getUsersByType("external");
		if (users == null) {
			return "redirect:/error?code=500";
		}
		model.addAttribute("users", users);
		logger.info("GET request:  All external users");
		
        return "manager/externalusers";
    }
	
	@GetMapping("/manager/user/{id}")
    public String getUserDetails(Model model, @PathVariable UUID id) {
		User user = userService.getUserByIdAndActive(id);
		if (user == null) {
			return "redirect:/error?code=400";
		}
		if (!user.getType().equals("external")) {
			logger.warn("GET request: Unauthorised request for external user detail");
			
			return "redirect:/error?code=401";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request:  External user detail by id");
		
        return "manager/userdetail";
    }
	
	@GetMapping("/manager/user/edit/{id}")
	public String editUser(Model model, @PathVariable UUID id) {
		User user = userService.getUserByIdAndActive(id);
		if (user == null) {
			return "redirect:/error?code=404";
		}
		if (!user.getType().equals("external")) {
			logger.warn("GET request: Admin unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: All external users");

		return "manager/externalusers_edit";
	}
	
	@PostMapping("/manager/user/edit/{id}")
    public String editSubmit(@ModelAttribute User user, @PathVariable UUID id, BindingResult bindingResult) {
		User current = userService.getUserByIdAndActive(id);
		if (current == null) {
			return "redirect:/error?code=404";
		}
		
		editUserFormValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			return "redirect:/error?code=400?path=form-validation";
        }
		if (!current.getType().equals("external")) {
			logger.warn("GET request: Admin unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		user.setUserId(id);
		logger.info("POST request: Internal user edit");
		user = userService.editUser(user);
		if (user == null) {
			return "redirect:/error?code=500";
		}
		
        return "redirect:/manager/user";
    }
	
	@GetMapping("/manager/user/delete/{id}")
	public String deleteUser(Model model, @PathVariable UUID id) {
		User user = userService.getUserByIdAndActive(id);
		if (user == null) {
			return "redirect:/error?code=404";
		}
		if (!user.getType().equals("external")) {
			logger.warn("GET request: Admin unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: Delete external user");
		
		return "manager/externalusers_delete";
	}
	
	@PostMapping("/manager/user/delete/{id}")
    public String deleteSubmit(@ModelAttribute User user, @PathVariable UUID id, BindingResult bindingResult) {
		User current = userService.getUserByIdAndActive(id);
		if (current == null) {
			return "redirect:/error?code=404";
		}
		if (!current.getType().equals("external")) {
			logger.warn("GET request: Admin unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
		userService.deleteUser(id);
		logger.info("POST request: Employee New modification request");
    	
        return "redirect:/manager/user";
    }

	
	@GetMapping("/manager/user/request/delete/{id}")
    public String deleteRequest(Model model, @PathVariable() UUID id) {
		ModificationRequest modificationRequest = userService.getModificationRequest(id);
		
		if (modificationRequest == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		
		// checks if manager is authorized for the request to approve
		if (!modificationRequest.getUserType().equals("external")) {
			logger.warn("GET request: Manager unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		
		model.addAttribute("modificationrequest", modificationRequest);
		logger.info("GET request: Manager external modification request by ID");
		
        return "manager/modificationrequest_delete";
    }
	
	@PostMapping("/manager/user/request/delete/{requestId}")
    public String deleteRequest(@PathVariable UUID requestId, @ModelAttribute ModificationRequest request, BindingResult bindingResult) {
		request = userService.getModificationRequest(requestId);
		
		// checks validity of request
		if (request == null) {
			return "redirect:/error?code=404&path=request-invalid";
		}
		
		// checks if manager is authorized for the request to approve
		if (!request.getUserType().equals("external")) {
			logger.warn("GET request: Manager unauthrorised request access");
			
			return "redirect:/error?code=401&path=request-unauthorised";
		}
		userService.deleteModificationRequest(request);
		logger.info("POST request: Manager approves modification request");
		
        return "redirect:/manager/user/request";
    }
}
