/**
 * 
 */
package securbank.controller;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;

import securbank.exceptions.Exceptions;
import securbank.models.CreditCard;
import securbank.models.CreditCardStatement;
import securbank.models.Transaction;
import securbank.models.Transfer;
import securbank.models.User;
import securbank.models.ViewAuthorization;
import securbank.services.CreditCardService;
import securbank.services.OtpService;
import securbank.services.PDFService;
import securbank.services.TransactionService;
import securbank.services.TransferService;
import securbank.services.UserService;
import securbank.services.ViewAuthorizationService;
import securbank.validators.EditUserFormValidator;
import securbank.validators.NewMerchantPaymentFormValidator;
import securbank.validators.NewTransactionFormValidator;
import securbank.validators.NewTransferFormValidator;
import securbank.validators.NewUserFormValidator;


/**
 * @author Ayush Gupta
 *
 */
@Controller
public class MerchantController {
	@Autowired
	UserService userService;
	
	@Autowired
	PDFService pdfService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	NewTransactionFormValidator transactionFormValidator;
	
	@Autowired
	private TransferService transferService;
	
	@Autowired
	NewTransferFormValidator transferFormValidator;
	
	@Autowired
	public HttpSession session;

	@Autowired 
	CreditCardService creditCardService;
	
	@Autowired 
	ViewAuthorizationService viewAuthorizationService;

	@Autowired 
	NewUserFormValidator userFormValidator;
	
	@Autowired 
	EditUserFormValidator editUserFormValidator;
	
	@Autowired
	NewMerchantPaymentFormValidator merchantPaymentFormValidator;
	
	@Autowired
	OtpService otpService;
	
	final static Logger logger = LoggerFactory.getLogger(MerchantController.class);

	@GetMapping("/merchant/details")
    public String currentUserDetails(Model model) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			//return "redirect:/error?code=400&path=user-notfound";
			throw new Exceptions("400","User Not Found !");
		}
		
		model.addAttribute("user", user);
		logger.info("GET request: External user detail");
		
        return "merchant/detail";
    }
	
	@GetMapping("/merchant/createtransaction")
	public String newTransactionForm(Model model){
		model.addAttribute("transaction", new Transaction());
		logger.info("GET request: Extrernal user transaction creation request");
		
		return "merchant/createtransaction";
	}
	
	@GetMapping("/merchant/transaction/otp")
	public String createTransactionOtp(Model model){
		model.addAttribute("transaction", new Transaction());
		logger.info("GET request: Extrernal user transaction generate OTP");
		User currentUser = userService.getCurrentUser();
		otpService.createOtpForUser(currentUser);
		
		return "redirect:/merchant/createtransaction";
	}
	
	@PostMapping("/merchant/createtransaction")
    public String submitNewTransaction(@ModelAttribute Transaction transaction, BindingResult bindingResult) throws Exceptions {
		logger.info("POST request: Submit transaction");
		
		transactionFormValidator.validate(transaction, bindingResult);

		if(bindingResult.hasErrors()){
			logger.info("POST request: createtransaction form with validation errors");
			return "redirect:/merchant/createtransaction";
		}
		
		if(transaction.getType().contentEquals("CREDIT")){
			if (transactionService.initiateCredit(transaction) == null) {
				//return "redirect:/error?code=400&path=transaction-error";
				throw new Exceptions("400","Transaction Error !");
			}
		}
		else {
			if (transactionService.initiateDebit(transaction) == null) {
				//return "redirect:/error?code=400&path=transaction-error";
				throw new Exceptions("400","Transaction Error !");
			}
		}
		
		//deactivate current otp
		otpService.deactivateOtpByUser(userService.getCurrentUser());
		
		return "redirect:/merchant/createtransaction?successTransaction=true";
    }
	
	@GetMapping("/merchant/createtransfer")
	public String newTransferForm(Model model){
		model.addAttribute("transfer", new Transfer());
		logger.info("GET request: Extrernal user transfer creation request");
		
		return "merchant/createtransfer";
	}

	@GetMapping("/merchant/transfer/otp")
	public String createTransferOtp(Model model){
		model.addAttribute("transfer", new Transfer());
		logger.info("GET request: Extrernal user transfer generate OTP");
		User currentUser = userService.getCurrentUser();
		otpService.createOtpForUser(currentUser);
		
		return "redirect:/merchant/createtransfer";
	}
	
	@PostMapping("merchant/createtransfer")
    public String submitNewTransfer(@ModelAttribute Transfer transfer, BindingResult bindingResult) throws Exceptions {
		logger.info("POST request: Submit transfer");
		
		transferFormValidator.validate(transfer, bindingResult);
		
		if(bindingResult.hasErrors()){
			logger.info("POST request: createtransfer form with validation errors");
			return "redirect:merchant/createtransfer";
		}
		
		if(transferService.initiateTransfer(transfer)==null){
			//return "redirect:/error?code=400&path=transfer-error";
			throw new Exceptions("400","Transfer Error !");
		}
		
		//deactivate current otp
		otpService.deactivateOtpByUser(userService.getCurrentUser());
		
		return "redirect:/merchant/createtransfer?successTransaction=true";
	}

	@GetMapping("/merchant/payment")
	public String newMerchantTransferForm(Model model){
		model.addAttribute("transfer", new Transfer());
		logger.info("GET request: Extrernal user transfer creation request");
		
		return "merchant/payment";
	}

	@GetMapping("/merchant/payment/otp")
	public String createMerchantTransferOtp(Model model){
		model.addAttribute("transfer", new Transfer());
		logger.info("GET request: Extrernal user transfer generate OTP");
		User currentUser = userService.getCurrentUser();
		otpService.createOtpForUser(currentUser);
		
		return "redirect:/merchant/payment";
	}
	
	@PostMapping("/merchant/payment")
    public String submitNewMerchantTransfer(@ModelAttribute Transfer transfer, BindingResult bindingResult) throws Exceptions {
		logger.info("POST request: Submit transfer");
		
		merchantPaymentFormValidator.validate(transfer, bindingResult);
		
		String otp = otpService.getOtpByUser(userService.getCurrentUser()).getCode();
		 if(!transfer.getOtp().equals(otp)){
			logger.info("Otp mismatch");
			//return "redirect:/error?code=400&path=transfer-error";
			throw new Exceptions("400","Transfer Error !");
		 }
		
		if(bindingResult.hasErrors()){
			logger.info("POST request: createtransfer form with validation errors");
			//return "redirect:/error?code=400&path=transfer-error";
			throw new Exceptions("400","Transfer Error !");
		}
		
		if(transferService.initiateMerchantPaymentRequest(transfer)==null){
			//return "redirect:/error?code=400&path=transfer-error";
			throw new Exceptions("400","Transfer Error !");
		}
		
		//deactivate current otp
		otpService.deactivateOtpByUser(userService.getCurrentUser());
		
		return "redirect:/merchant/payment?successPayment=true";
	}
	
	@GetMapping("/merchant/edit")
    public String editUser(Model model) {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/error";
		}
		model.addAttribute("user", user);
		
        return "merchant/edit";
    }
	
	@PostMapping("/merchant/edit")
    public String editSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		editUserFormValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			return "merchant/edit";
        }
		
		// create request
    	userService.createExternalModificationRequest(user);
	
        return "redirect:/merchant/details?successEdit=true";
    }
	

	@GetMapping("/merchant/transfers")
    public String getTransfers(Model model) throws Exceptions {
		logger.info("GET request:  All pending transfers");
		
		List<Transfer> transfers = transferService.getTransfersByStatusAndUser(userService.getCurrentUser(),"Waiting");
		if (transfers == null) {
			//return "redirect:/error?code=404&path=transfers-not-found";
			throw new Exceptions("404","Transfer Not Found !");
		}
		model.addAttribute("transfers", transfers);
		
        return "merchant/pendingtransfers";
    }
	
	@PostMapping("/merchant/transfer/request/{id}")
    public String approveRejectTransfer(@ModelAttribute Transfer trans, @PathVariable() UUID id, BindingResult bindingResult) throws Exceptions {
		
		Transfer transfer = transferService.getTransferById(id);
		if (transfer == null) {
			//return "redirect:/error?code=404&path=request-invalid";
			throw new Exceptions("404","Transfer Not Found !");
		}
		
		// checks if user is authorized for the request to approve
		if (!transfer.getFromAccount().getUser().getEmail().equalsIgnoreCase(userService.getCurrentUser().getEmail())) {
			logger.warn("Transafer made TO non external account");
			//return "redirect:/error?code=401&path=request-unauthorised";
			throw new Exceptions("401"," ");
		}
		
		if (!transfer.getToAccount().getUser().getRole().equalsIgnoreCase("ROLE_MERCHANT")) {
			logger.warn("Transafer made FROM non merchant account");
					
			//return "redirect:/error?code=401&path=request-unauthorised";
			throw new Exceptions("401"," ");
		}
		
		if("approved".equalsIgnoreCase(trans.getStatus())){
			//check if transfer is valid in case modified
			if(transferService.isTransferValid(transfer)==false){
				//return "redirect:/error?code=401&path=amount-invalid";
				throw new Exceptions("401","Amount Invalid !");
			}
			transferService.approveTransferToPending(transfer);
		}
		else if ("rejected".equalsIgnoreCase(trans.getStatus())) {
			transferService.declineTransfer(transfer);
		}
		
		logger.info("GET request: Manager approve/decline external transaction requests");
		
        return "redirect:/merchant/transfers?successAction=true";
    }
	
	@GetMapping("/merchant/transfer/{id}")
    public String getTransferRequest(Model model, @PathVariable() UUID id) throws Exceptions {
		Transfer transfer = transferService.getTransferById(id);
		
		if (transfer == null) {
			//return "redirect:/error?code=404&path=request-invalid";
			throw new Exceptions("404","Invalid Request !");
		}

		// checks if user is authorized for the request to approve
		if (!transfer.getFromAccount().getUser().getEmail().equalsIgnoreCase(userService.getCurrentUser().getEmail())) {
			logger.warn("Transafer made TO non external account");
			//return "redirect:/error?code=401&path=request-unauthorised";
			throw new Exceptions("401"," ");
		}
		
				
		if (!transfer.getToAccount().getUser().getRole().equalsIgnoreCase("ROLE_MERCHANT")) {
			logger.warn("Transafer made FROM non merchant account");
					
			//return "redirect:/error?code=401&path=request-unauthorised";
			throw new Exceptions("401"," ");
		}
				
		model.addAttribute("transfer", transfer);
		logger.info("GET request: User merchant transfer request by ID");
		
        return "merchant/approverequests";
	}

	@GetMapping("/merchant/request")
    public String getRequest(Model model) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			//return "redirect:/error";
			throw new Exceptions("401"," ");
		}
		
		model.addAttribute("viewrequests", viewAuthorizationService.getPendingAuthorization(user));
		
        return "merchant/accessrequests";
    }
	
	@GetMapping("/merchant/request/view/{id}")
    public String getRequest(@PathVariable UUID id, Model model) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		
		ViewAuthorization authorization = viewAuthorizationService.getAuthorizationById(id);
		if (authorization == null) {
			//return "redirect:/error?code=404";
			throw new Exceptions("404"," ");
		}
		if (authorization.getExternal() != user) {
			//return "redirect:/error?code=401";
			throw new Exceptions("401"," ");
		}
		
		model.addAttribute("viewrequest", authorization);
		
        return "merchant/accessrequest_detail";
    }
	
	@PostMapping("/merchant/request/{id}")
    public String getRequests(@PathVariable UUID id, @ModelAttribute ViewAuthorization request, BindingResult bindingResult) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		String status = request.getStatus();
		if (status == null || !(status.equals("approved") || status.equals("rejected"))) {
			//return "redirect:/error?code=400";
			throw new Exceptions("400"," ");
		}
		
		ViewAuthorization authorization = viewAuthorizationService.getAuthorizationById(id);
		if (authorization == null) {
			//return "redirect:/error?code=404";
			throw new Exceptions("404"," ");
		}
		if (authorization.getExternal() != user) {
			//return "redirect:/error?code=401";
			throw new Exceptions("401"," ");
		}
		
		authorization.setStatus(status);
		authorization = viewAuthorizationService.approveAuthorization(authorization);
		
        return "redirect:/merchant/request?successAction=true";
    }

	@RequestMapping("/merchant/downloadPDF")
	public void downloadPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {

		final ServletContext servletContext = request.getSession().getServletContext();
		final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		final String temperotyFilePath = tempDirectory.getAbsolutePath();
		
		User user = userService.getCurrentUser();

		String fileName = "account_statement.pdf";
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);

		
		try {

			pdfService.createStatementPDF(temperotyFilePath + "\\" + fileName, user);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos = pdfService.convertPDFToByteArrayOutputStream(temperotyFilePath + "\\" + fileName);
			OutputStream os = response.getOutputStream();
			baos.writeTo(os);
			os.flush();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@GetMapping("/merchant/credit-card/create")
    public String createCreditCard(Model model) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new Exceptions("404", "User Not Found");
		}
		if (creditCardService.getCreditCardDetails(user) != null) {
			return "redirect:/merchant/credit-card/details";
		}
		
		logger.info("GET request: create credit card");
		
        return "merchant/creditcard_create";
    }
	
	@PostMapping("/merchant/credit-card/create")
    public String createCreditCard(@ModelAttribute CreditCard cc, BindingResult bindingResult)throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new Exceptions("404", "User Not Found");
		}
		if (creditCardService.getCreditCardDetails(user) != null) {
			return "redirect:/merchant/credit-card/details";
		}
		
		logger.info("POST request: create credit card");
		
    	creditCardService.createCreditCard(user);
    	
        return "redirect:/merchant/credit-card/details";
    }
	
	@GetMapping("/merchant/credit-card/details")
    public String detailCreditCard(Model model) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new Exceptions("404", "User Not Found");
		}
		CreditCard creditCard = creditCardService.getCreditCardDetails(user); 
		if (creditCard == null) {
			return "redirect:/merchant/credit-card/create";
		}
		model.addAttribute("creditCard", creditCard);
		logger.info("GET request: credit card detail");
		
        return "merchant/creditcard_detail";
    }
	
	@GetMapping("/merchant/credit-card/transaction/create")
    public String createCreditCardTransacttion(Model model) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new Exceptions("404", "User Not Found");
		}
		if (creditCardService.getCreditCardDetails(user) == null) {
			return "redirect:/merchant/credit-card/create";
		}
		model.addAttribute("transaction", new Transaction());
		logger.info("GET request: create credit card transaction");
		
        return "merchant/creditcard_transaction_create";
    }
	
	@PostMapping("/merchant/credit-card/transaction/create")
    public String createCreditCardTransaction(@ModelAttribute Transaction transaction, BindingResult bindingResult) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new Exceptions("404", "User Not Found");
		}
		CreditCard cc = creditCardService.getCreditCardDetails(user);
		if (creditCardService.getCreditCardDetails(user) == null) {
			return "redirect:/merchant/credit-card/create";
		}
		transaction.setType("DEBIT");
		transactionFormValidator.validate(transaction, bindingResult);
		logger.info("POST request: make a payment for credit card");
    	creditCardService.createCreditCardTransaction(transaction, cc);
    	
        return "redirect:/merchant/credit-card/details";
    }
	
	@GetMapping("/merchant/credit-card/transaction")
    public String getCreditCardTransacttions(Model model) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new Exceptions("404", "User Not Found");
		} 
		CreditCard cc = creditCardService.getCreditCardDetails(user);
		if (cc == null) {
			return "redirect:/merchant/credit-card/create";
		}
		List<Transaction> transactions = transactionService.getTransactionsByAccount(cc.getAccount());
		model.addAttribute("transactions", transactions);
		logger.info("GET request: get credit card all transactions");
		
        return "merchant/creditcard_transactions";
    }
	
	@GetMapping("/merchant/credit-card/makepayment")
    public String createCreditCardMakePayment(Model model) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		CreditCard cc = creditCardService.getCreditCardDetails(user); 
		if (cc == null) {
			return "redirect:/merchant/credit-card/create";
		}
		cc = creditCardService.getDueAmount(cc);
		model.addAttribute("creditcard", cc);
		logger.info("GET request: make a payment for credit card");
		
	    return "merchant/creditcard_transaction_makepayment";
	}
	

	@PostMapping("/merchant/credit-card/makepayment")
    public String createCreditCardMakePayment(@ModelAttribute Transaction transaction, BindingResult bindingResult) throws Exceptions {
		// TODO validate transaction
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		CreditCard cc = creditCardService.getCreditCardDetails(user);
		if (creditCardService.getCreditCardDetails(user) == null) {
			return "redirect:/merchant/credit-card/create";
		}
		logger.info("POST request: make a payment for credit card");
		
    	transaction = creditCardService.creditCardMakePayment(cc);
    	if (transaction == null) {
    		throw new Exceptions("400", "Bad Request");
    	}
    	
        return "redirect:/merchant/credit-card/details";
    }
	
	@GetMapping("/merchant/credit-card/statement")
    public String getCreditCardStatements(Model model) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		CreditCard cc = creditCardService.getCreditCardDetails(user);
		if (cc == null) {
			return "redirect:/merchant/credit-card/create";
		}
		logger.info("GET request: get statements for credit card");
    	model.addAttribute("statements", cc.getStatements());
    	
        return "merchant/creditcard_statements";
    }
	
	@GetMapping("/merchant/credit-card/statement/{id}")
    public String getCreditCardStatements(@PathVariable UUID id, Model model) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			return "redirect:/login";
		}
		CreditCard cc = creditCardService.getCreditCardDetails(user);
		if (creditCardService.getCreditCardDetails(user) == null) {
			return "redirect:/merchant/credit-card/create";
		}
		
		// TODO: adds validation of transaction
		logger.info("GET request: get statements for credit card");
		CreditCardStatement statement = creditCardService.getStatementById(cc, id);
		if (statement == null) {
			throw new Exceptions("400", "Bad Request");
		}
    	model.addAttribute("statement", statement);
    	
        return "merchant/creditcard_statementdetail";
	}
	
	@GetMapping("/merchant/credit-card/statement/{id}/pdf")
    public void getCreditCardStatementPdf(@PathVariable UUID id, HttpServletRequest request, HttpServletResponse response) throws Exceptions {
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new Exceptions("404", "User not found");
		}
		CreditCard cc = creditCardService.getCreditCardDetails(user);
		if (creditCardService.getCreditCardDetails(user) == null) {
			throw new Exceptions("404", "Credit Card not found");
		}
		CreditCardStatement statement = creditCardService.getStatementById(cc, id);
		if (statement == null) {
			throw new Exceptions("400", "Bad Request");
		}
		
		final ServletContext servletContext = request.getSession().getServletContext();
		final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		final String temperotyFilePath = tempDirectory.getAbsolutePath();
		
		String fileName = "statement.pdf";
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		
		try {
			pdfService.createCreditCardStatementPDF(temperotyFilePath + "\\" + fileName, statement);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos = pdfService.convertPDFToByteArrayOutputStream(temperotyFilePath + "\\" + fileName);
			OutputStream os = response.getOutputStream();
			baos.writeTo(os);
			os.flush();
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}		
	}
}
