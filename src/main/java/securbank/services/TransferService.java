package securbank.services;

import java.util.List;
import java.util.UUID;

import securbank.models.Transfer;

/**
 * @author Mitikaa
 *
 */
public interface TransferService {
	public Transfer initiateTransfer(Transfer transfer);
	public Transfer approveTransfer(Transfer transfer);
	public Transfer declineTransfer(Transfer transfer);
	public List<Transfer> getPendingTransfersByAccountNumber(Long accountNumber);
	public List<Transfer> getPendingTransfersByType(Long accountNumber, String accountType);
	public List<Transfer> getTransfersByStatus(String approvalStatus);
	public Transfer getTransferById(UUID id);
	public Transfer getPendingTransfernByAccountNumber(Long accountNumber);
	public boolean isTransferValid(Transfer transfer);
	public boolean isToAccountValid(Transfer transfer);
}
