package securbank.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import securbank.models.CreditCard;
import securbank.models.User;

@Repository("creditCardDao")
public class CreditCardDaoImpl extends BaseDaoImpl<CreditCard, UUID> implements CreditCardDao {
	@Autowired
	EntityManager entityManager;
	
	public CreditCardDaoImpl() {
		super(CreditCard.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CreditCard> findAll() {
		return (List<CreditCard>) this.entityManager.createQuery("SELECT creditcard from CreditCard creditcard")
				.getResultList();
	
	}

	@Override
	public CreditCard findByAccountNumber(Long accountNumber) {
		try {
			return (CreditCard) this.entityManager.createQuery("SELECT creditcard from CreditCard creditcard where (creditcard.account_number = :accountNumber) AND creditcard.active = 1")
					.setParameter("accountNumber", accountNumber)
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no user if found
			return null;
		}
	}
	
	@Override
	public CreditCard findByUser(User user) {
		try {
			return (CreditCard) this.entityManager.createQuery("SELECT creditcard from CreditCard creditcard where creditcard.account.user = :user AND creditcard.active = true")
					.setParameter("user", user)
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no user if found
			return null;
		}
	}
}
