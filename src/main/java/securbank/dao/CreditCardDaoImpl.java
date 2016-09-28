package securbank.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import securbank.models.CreditCard;

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
	public CreditCard findByAccountNumber(UUID accountID) {
		try {
			return (CreditCard) this.entityManager.createQuery("SELECT creditcard from CreditCard creditcard where (creditcard.accountID = :accountID) AND creditcard.active = 1")
					.setParameter("accountID", accountID)
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no user if found
			return null;
		}
	}
}
