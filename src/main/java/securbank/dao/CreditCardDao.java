package securbank.dao;

import java.util.List;
import java.util.UUID;

import securbank.models.CreditCard;

public interface CreditCardDao extends BaseDao<CreditCard, UUID> {
	public List<CreditCard> findAll();
	public CreditCard findByAccountNumber(Long accountNumber);
}
