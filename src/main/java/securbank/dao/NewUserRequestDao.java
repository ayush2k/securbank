/**
 * 
 */
package securbank.dao;

import java.util.UUID;

import securbank.models.NewUserRequest;

/**
 * @author Ayush Gupta
 *
 */
public interface NewUserRequestDao extends BaseDao<NewUserRequest, UUID>{
	public NewUserRequest findById(UUID newUserRequestId);
	public NewUserRequest findByEmailAndRole(String email, String role);
	public boolean emailExists(String email);
}
