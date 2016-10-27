package securbank.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import securbank.models.CreditCard;
import securbank.models.CreditCardStatement;

@Repository("creditCardStatementDao")
public class CreditCardStatementDaoImpl extends BaseDaoImpl<CreditCardStatement, UUID> implements CreditCardStatementDao {
	@Autowired
	EntityManager entityManager;
	
	public CreditCardStatementDaoImpl() {
		super(CreditCardStatement.class);
	}
	
	public List<CreditCardStatement> findByGenerationDateAndStatus(LocalDate date, String status) {
		return this.entityManager.createQuery("SELECT statement from CreditCardStatement statement "+
					"WHERE statement.cc.statementGeneration = :date " +
					"AND statement.status= :status " +
					"AND statement.pendingDate <= :now", CreditCardStatement.class)
				.setParameter("date", date)
				.setParameter("status", status)
				.setParameter("now", LocalDate.now())
				.getResultList();
	}
	
	public List<CreditCardStatement> findByPendingDateAndStatus(LocalDate date, String status) {
		return this.entityManager.createQuery("SELECT statement from CreditCardStatement statement " +
					"WHERE statement.status = :status " +
					"AND statement.pendingDate <= :date", CreditCardStatement.class)
				.setParameter("status", status)
				.setParameter("date", date)
				.getResultList();
	}
	
	public List<CreditCardStatement> findByCreditCardAndStatus(CreditCard cc, String status){
		return this.entityManager.createQuery("SELECT statement from CreditCardStatement statement " +
				"WHERE statement.status= :status " +
				"AND statement.cc = :cc", CreditCardStatement.class)
			.setParameter("status", status)
			.setParameter("cc", cc)
			.getResultList();
	}
}
