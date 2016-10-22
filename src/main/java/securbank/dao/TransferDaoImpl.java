package securbank.dao;

import org.springframework.stereotype.Repository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import securbank.models.Account;
import securbank.models.Transfer;

/**
 * @author Mitikaa Sama
 *
 * Sep 26, 2016
 */

@Repository("transferDao")
public class TransferDaoImpl extends BaseDaoImpl<Transfer, UUID> implements TransferDao{

	@Autowired
	EntityManager entityManager;
	
	
	public TransferDaoImpl() {
		super(Transfer.class);
	}
	
	/**
     * Returns list of all transfers in the table
     * 
     * @return transfers
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Transfer> findAll() {
		return (List<Transfer>) this.entityManager.createQuery("SELECT transfer from Transfer transfer")
				.getResultList();
	}

	/**
     * Returns list of transfers in the table filtered by from account number
     * 
     * @return transfers
     */
	@Override
	public Transfer findTransferByFromAccount(String fromAccountnumber) {
		try {
			return (Transfer) this.entityManager.createQuery("SELECT transfer from Transfer transfer"
					+ " where transfer.fromAccountnumber = :accountNumber")
					.setParameter("fromAccountnumber", fromAccountnumber)
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no trasnfer is found
			return null;
		}
	}
	
	/**
     * Returns list of transfers in the table filtered by from account number
     * 
     * @return transfers
     */
	@Override
	public Transfer findTransferByToAccount(String toAccountnumber) {
		try {
			return (Transfer) this.entityManager.createQuery("SELECT transfer from Transfer transfer"
					+ " where transfer.toAccountnumber = :toAccountnumber")
					.setParameter("toAccountnumber", toAccountnumber)
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no trasnfer is found
			return null;
		}
	}
}
