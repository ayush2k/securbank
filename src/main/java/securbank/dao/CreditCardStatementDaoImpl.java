package securbank.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import securbank.models.CreditCardStatement;

@Repository("creditCardStatementDao")
public class CreditCardStatementDaoImpl extends BaseDaoImpl<CreditCardStatement, UUID> implements CreditCardStatementDao {
	@Autowired
	EntityManager entityManager;
	
	public CreditCardStatementDaoImpl() {
		super(CreditCardStatement.class);
	}
	
}
