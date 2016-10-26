package securbank.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import securbank.models.Account;
import securbank.models.Transfer;
import securbank.models.User;

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
	public List<Transfer> findTransferByFromAccount(Account account) {
		return this.entityManager.createQuery("SELECT transfer from Transfer transfer"
				+ " where transfer.fromAccount = :account", Transfer.class)
				.setParameter("account", account)
				.getResultList();
	}
	
	/**
     * Returns list of transfers in the table filtered by from account number
     * 
     * @return transfers
     */
	@Override
	public List<Transfer> findTransferByToAccount(Account toAccountnumber) {
		return this.entityManager.createQuery("SELECT transfer from Transfer transfer"
				+ " where transfer.toAccountnumber = :toAccountnumber", Transfer.class)
				.setParameter("toAccountnumber", toAccountnumber)
				.getResultList();
		
	}

	@Override
	public List<Transfer> findByApprovalStatus(String status) {
		return this.entityManager.createQuery("SELECT transfer from Transfer transfer"
				+ " where transfer.status = :status", Transfer.class)
				.setParameter("status", status)
				.getResultList();
	}

	@Override
	public List<Transfer> findPendingTransferByFromAccount(Account fromAccount) {
		return this.entityManager.createQuery("SELECT transfer from Transfer transfer"
				+ " where transfer.fromAccount = :account AND transfer.status = :status", Transfer.class)
				.setParameter("account", fromAccount)
				.setParameter("status", "Pending")
				.getResultList();
	}
	
	@Override
	public List<Transfer> findByUserAndApprovalStatus(User user, String status) {
		return this.entityManager.createQuery("SELECT transfer from Transfer transfer"
				+ " where transfer.status = :status AND (transfer.toAccount.user = :user OR transfer.fromAccount.user = :user)", Transfer.class)
				.setParameter("status", status)
				.setParameter("user", user)
				.getResultList();
	}
}
