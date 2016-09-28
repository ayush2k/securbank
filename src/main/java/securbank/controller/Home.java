package securbank.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Ayush Gupta
 *
 */
@Controller
public class Home {
	
	@RequestMapping("/")
    public String homeController(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
       return "home";
    }
}
