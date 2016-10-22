package securbank.dao;

import org.joda.time.LocalDate;
import java.util.List;
import java.util.UUID;

import securbank.models.CreditCard;
import securbank.models.User;

public interface CreditCardDao extends BaseDao<CreditCard, UUID> {
	public List<CreditCard> findAll();
	public CreditCard findByAccountNumber(Long accountNumber);
	public CreditCard findByUser(User user);
	public List<CreditCard> findByGenerationDate(LocalDate date);
}
