package securbank.dao;

import securbank.models.Account;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;


/**
 * 
 * @author Madhu Illuri
 *
 */
@Repository("accountDao")
public class AccountDaoImpl extends BaseDaoImpl<Account, UUID> implements AccountDao {
	@Autowired
	EntityManager entityManager;
	
	public AccountDaoImpl(){
		super(Account.class);
	}
	
	/**
	 * function to return all the accounts 
	 * @return accounts 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Account> findAll(){
		String query = "SELECT acc from Account acc";
		return (List<Account>) this.entityManager.createQuery(query).getResultList();
					
	}
}
