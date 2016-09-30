package securbank.dao;

import securbank.models.Account;

import java.util.*;

/**
 * 
 * @author Madhu Illuri
 *
 */
public interface AccountDao extends BaseDao<Account, UUID> {
	
	public List<Account> findAll();

}