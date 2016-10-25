package securbank.controller;

import static org.mockito.Matchers.intThat;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.internal.matchers.Find;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import securbank.models.DeviceTrust;
import securbank.models.User;
import securbank.services.DeviceTrustService;
import securbank.services.UserService;
import securbank.validators.NewUserFormValidator;
/**
 * @author Ayush Gupta
 *
 */
@Controller
public class CommonController {
	@Autowired
	UserService userService;
	
	@Autowired
	DeviceTrustService deviceTrustService;
	
	
	@Autowired 
	NewUserFormValidator userFormValidator;
	
	final static Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}
	
	@GetMapping("/signup")
    public String signupForm(Model model) {
		model.addAttribute("user", new User());
		logger.info("GET request: signup form");
		
		return "signup";
    }
		
	@PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		userFormValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			logger.info("POST request: signup form with validation errors");
			
			return "signup";
        }
		// If the control came here, there are no errors in the form
		// submitted
		
		
		logger.info("POST request: signup");
		logger.info("Username: "+user.getUsername());
		
    	userService.createExternalUser(user);
    	InetAddress ip;
		try {
		ip = InetAddress.getLocalHost();
		System.out.println("Current IP address : " + ip.getHostAddress());

		NetworkInterface network = NetworkInterface.getByInetAddress(ip);

		byte[] mac = network.getHardwareAddress();
		String macString = new String(mac);
		System.out.println(macString);
    	DeviceTrust deviceTrust = new DeviceTrust();
    	deviceTrust.setMacAddress(macString);
    	deviceTrust.setUser(user);
		}catch(Exception e){
			System.out.println(e);
		}
    	
        return "redirect:/";
    }
	
	@GetMapping("/user/verify/{id}")
    public String verifyNewUser(Model model, @PathVariable UUID id) {
		if (userService.verifyNewUser(id) == false) {
			logger.info("GET request: verification failed of new external user");
			return "redirect:/error?code=400";
		}
		logger.info("GET request: verification of new external user");
		
		return "redirect:/";
    }
}
