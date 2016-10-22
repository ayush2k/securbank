package securbank.dao;

import java.util.List;
import java.util.UUID;

import org.joda.time.LocalDate;

import securbank.models.CreditCard;
import securbank.models.CreditCardStatement;

public interface CreditCardStatementDao extends BaseDao<CreditCardStatement, UUID> {
	public List<CreditCardStatement> findByGenerationDateAndStatus(LocalDate date, String status);
	public List<CreditCardStatement> findByCreditCardAndStatus(CreditCard cc, String status);
	public List<CreditCardStatement> findByPendingDateAndStatus(LocalDate date, String status);
}
