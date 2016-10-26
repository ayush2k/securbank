/**
 * 
 */
package securbank.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

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

import securbank.models.CreditCard;
import securbank.models.CreditCardStatement;
import securbank.models.Transaction;
import securbank.models.Transfer;
import securbank.models.User;
import securbank.models.ViewAuthorization;
import securbank.services.CreditCardService;
import securbank.services.TransactionService;
import securbank.services.TransferService;
import securbank.services.UserService;
import securbank.services.ViewAuthorizationService;
import securbank.validators.EditUserFormValidator;
import securbank.validators.NewTransactionFormValidator;
import securbank.validators.NewTransferFormValidator;
import securbank.validators.NewUserFormValidator;

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
	
	@Autowired
	TransactionService transactionService;
	
	final static Logger logger = LoggerFactory.getLogger(ExternalUserController.class);
	
	
	@Autowired
	NewTransactionFormValidator transactionFormValidator;
	
	@Autowired
	private TransferService transferService;
	
	@Autowired
	NewTransferFormValidator transferFormValidator;
	
	@Autowired
	public HttpSession session;

	@Autowired 
	ViewAuthorizationService viewAuthorizationService;

	@Autowired 
	NewUserFormValidator userFormValidator;
	
	@Autowired 
	EditUserFormValidator editUserFormValidator;

	@GetMapping("/user/details")
    public String currentUserDetails(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=400&path=user-notfound";
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: External user detail");
		
        return "external/detail";
    }
	
	@GetMapping("/user/createtransaction")
	public String newTransactionForm(Model model){
		model.addAttribute("transaction", new Transaction());
		logger.info("GET request: Extrernal user transaction creation request");
		
		return "external/createtransaction";
	}
	
	@PostMapping("/user/createtransaction")
    public String submitNewTransaction(@ModelAttribute Transaction transaction, BindingResult bindingResult) {
		logger.info("POST request: Submit transaction");
		
		transactionFormValidator.validate(transaction, bindingResult);
		
		if(bindingResult.hasErrors()){
			logger.info("POST request: createtransaction form with validation errors");
			return "external/createtransaction";
		}
		
		if(transaction.getType().contentEquals("CREDIT")){
			if (transactionService.initiateCredit(transaction) == null) {
				return "redirect:/";
			}
		}
		else {
			if (transactionService.initiateDebit(transaction) == null) {
				return "redirect:/";
			}
		}
		
		return "redirect:/user/createtransaction";
    }
	
	@GetMapping("/user/createtransfer")
	public String newTransferForm(Model model){
		model.addAttribute("transfer", new Transfer());
		logger.info("GET request: Extrernal user transfer creation request");
		return "external/createtransfer";
	}
	
	@PostMapping("user/createtransfer")
    public String submitNewTransfer(@ModelAttribute Transfer transfer, BindingResult bindingResult) {
		logger.info("POST request: Submit transfer");
		
		transferFormValidator.validate(transfer, bindingResult);
		
		if(bindingResult.hasErrors()){
			logger.info("POST request: createtransfer form with validation errors");
			return "external/createtransfer";
		}
		
		if(transferService.initiateTransfer(transfer)==null){
			return "redirect:/";
		}
		
		return "redirect:/user/createtransfer";
	}

	@GetMapping("/user/edit")
    public String editUser(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error";
		}
		model.addAttribute("user", user);
		
        return "external/edit";
    }
	
	@PostMapping("/user/edit")
    public String editSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		editUserFormValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			return "external/edit";
        }
		
		// create request
    	userService.createExternalModificationRequest(user);
	
        return "redirect:/";
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
	
	@GetMapping("/user/credit-card/transaction/create")
    public String createCreditCardTransacttion(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user-notfound";
		}
		if (creditCardService.getCreditCardDetails(user) == null) {
			return "redirect:/user/credit-card/create";
		}
		model.addAttribute("transaction", new Transaction());
		logger.info("GET request: create credit card transaction");
		
        return "external/creditcard_transaction_create";
    }
	
	@PostMapping("/user/credit-card/transaction/create")
    public String createCreditCardTransaction(@ModelAttribute Transaction transaction, BindingResult bindingResult) {
		// TODO validate transaction
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user-notfound";
		}
		CreditCard cc = creditCardService.getCreditCardDetails(user);
		if (creditCardService.getCreditCardDetails(user) == null) {
			return "redirect:/user/credit-card/create";
		}
		// TODO: adds validation of transaction
		logger.info("POST request: make a payment for credit card");
    	creditCardService.createCreditCardTransaction(transaction, cc);
    	
        return "redirect:/user/credit-card/details";
    }
	
	@GetMapping("/user/credit-card/transaction")
    public String getCreditCardTransacttions(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error?code=user-notfound";
		} 
		CreditCard cc = creditCardService.getCreditCardDetails(user);
		if (cc == null) {
			return "redirect:/user/credit-card/create";
		}
		List<Transaction> transactions = transactionService.getTransactionsByAccount(cc.getAccount());
		model.addAttribute("transactions", transactions);
		logger.info("GET request: get credit card all transactions");
		
        return "external/creditcard_transactions";
    }
	
	@GetMapping("/user/credit-card/makepayment")
    public String createCreditCardMakePayment(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		CreditCard cc = creditCardService.getCreditCardDetails(user); 
		if (cc == null) {
			return "redirect:/user/credit-card/create";
		}
		cc = creditCardService.getDueAmount(cc);
		model.addAttribute("creditcard", cc);
		logger.info("GET request: make a payment for credit card");
		
	    return "external/creditcard_transaction_makepayment";
	}
	

	@PostMapping("/user/credit-card/transaction/makepayment")
    public String createCreditCardMakePayment(@ModelAttribute Transaction transaction, BindingResult bindingResult) {
		// TODO validate transaction
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		CreditCard cc = creditCardService.getCreditCardDetails(user);
		if (creditCardService.getCreditCardDetails(user) == null) {
			return "redirect:/user/credit-card/create";
		}
		// TODO: adds validation of transaction
		logger.info("POST request: make a payment for credit card");
		
    	transaction = creditCardService.creditCardMakePayment(cc);
    	if (transaction == null) {
    		return "redirect:/error?code=400";
    	}
    	
        return "redirect:/user/credit-card/details";
    }
	
	@GetMapping("/user/credit-card/statement")
    public String getCreditCardStatements(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		CreditCard cc = creditCardService.getCreditCardDetails(user);
		if (cc == null) {
			return "redirect:/user/credit-card/create";
		}
		logger.info("GET request: get statements for credit card");
    	model.addAttribute("statements", cc.getStatements());
    	
        return "external/creditcard_statements";
    }
	
	@GetMapping("/user/credit-card/statement/{id}")
    public String getCreditCardStatements(@PathVariable UUID id, Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		CreditCard cc = creditCardService.getCreditCardDetails(user);
		if (creditCardService.getCreditCardDetails(user) == null) {
			return "redirect:/user/credit-card/create";
		}
		
		// TODO: adds validation of transaction
		logger.info("POST request: get statements for credit card");
		CreditCardStatement statement = creditCardService.getStatementById(cc, id);
		if (statement == null) {
			return "redirect:/error?code=400";
		}
    	model.addAttribute("statement", statement);
    	
        return "external/creditcard_statementdetail";
	}

	@GetMapping("/user/request")
    public String getRequest(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error";
		}
		
		model.addAttribute("viewrequests", viewAuthorizationService.getPendingAuthorization(user));
		
        return "external/accessrequests";
    }
	
	@GetMapping("/user/request/view/{id}")
    public String getRequest(@PathVariable UUID id, Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		
		ViewAuthorization authorization = viewAuthorizationService.getAuthorizationById(id);
		if (authorization == null) {
			return "redirect:/error?code=404";
		}
		if (authorization.getExternal() != user) {
			return "redirect:/error?code=401";
		}
		
		model.addAttribute("viewrequest", authorization);
		
        return "external/accessrequest_detail";
    }
	
	@PostMapping("/user/request/{id}")
    public String getRequests(@PathVariable UUID id, @ModelAttribute ViewAuthorization request, BindingResult bindingResult) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		String status = request.getStatus();
		if (status == null || !(status.equals("approved") || status.equals("rejected"))) {
			return "redirect:/error?code=400";
		}
		
		ViewAuthorization authorization = viewAuthorizationService.getAuthorizationById(id);
		if (authorization == null) {
			return "redirect:/error?code=404";
		}
		if (authorization.getExternal() != user) {
			return "redirect:/error?code=401";
		}
		
		authorization.setStatus(status);
		authorization = viewAuthorizationService.approveAuthorization(authorization);
		
        return "redirect:/user/request";
    }
}
