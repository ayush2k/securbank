/**
 * 
 */
package securbank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import securbank.models.CreditCard;
import securbank.models.User;
import securbank.services.CreditCardService;
import securbank.services.UserService;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class ExternalUserController {
	@Autowired
	UserService userService;
	
	@Autowired
	CreditCardService creditCardService;
	final static Logger logger = LoggerFactory.getLogger(ExternalUserController.class);
	
	@GetMapping("/user/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user-notfound";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: External user detail");
		
        return "external/detail";
    }
	
	@GetMapping("/user/credit-card/create")
    public String createCreditCard(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user-notfound";
		}
		if (creditCardService.getCreditCardDetails(user) != null) {
			return "redirect:/user/credit-card/details";
		}
		
		logger.info("GET request: create credit card");
		
        return "external/creditcard_create";
    }
	
	@PostMapping("/user/credit-card/create")
    public String createCreditCard(@ModelAttribute CreditCard cc, BindingResult bindingResult) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user-notfound";
		}
		if (creditCardService.getCreditCardDetails(user) != null) {
			return "redirect:/user/credit-card/details";
		}
		
		logger.info("POST request: create credit card");
		
    	creditCardService.createCreditCard(user);
    	
        return "redirect:/user/credit-card/details";
    }
	
	@GetMapping("/user/credit-card/details")
    public String detailCreditCard(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user-notfound";
		}
		CreditCard creditCard = creditCardService.getCreditCardDetails(user); 
		if (creditCard == null) {
			return "redirect:/user/credit-card/create";
		}
		model.addAttribute("creditCard", creditCard);
		logger.info("GET request: credit card detail");
		
        return "external/creditcard_detail";
    }
	
}
