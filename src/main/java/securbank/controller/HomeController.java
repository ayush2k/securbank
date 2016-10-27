package securbank.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class HomeController {
	
	@GetMapping("/")
    public String blankController(Model model) {
       return "redirect:/login";
    }
	

	@GetMapping("/home")
    public String homeController(Model model) {
       return "redirect:/login";
    }
}
