/**
 * 
 */
package securbank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author Ayush Gupta
 *
 */
@Service("emailService")
public class EmailServiceImpl implements EmailService {
	@Autowired
	private MailSender mailSender;
	
	@Value("${send.from.email}")
	private String from;
	
	public boolean sendEmail(SimpleMailMessage message) {
		message.setFrom(from);
		try {
			mailSender.send(message);
		}
		catch (MailException e){
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}
}
