package securbank.dao;

import java.util.List;
import java.util.UUID;

import securbank.models.Transfer;

/**
 * @author Mitikaa Sama
 *
 * Sep 26, 2016
 */
public interface TransferDao extends BaseDao<Transfer, UUID>{
	public List<Transfer> findAll();
	public Transfer findTransferByFromAccount(String fromAccountnumber);
	public Transfer findTransferByToAccount(String toAccountnumber);
}
