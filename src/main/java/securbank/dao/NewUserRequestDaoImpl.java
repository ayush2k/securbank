/**
 * 
 */
package securbank.dao;

import java.util.UUID;

import javax.persistence.NoResultException;

import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import securbank.models.NewUserRequest;

/**
 * @author Ayush Gupta
 *
 */
@Repository("newUserRequestDao")
public class NewUserRequestDaoImpl extends BaseDaoImpl<NewUserRequest, UUID> implements NewUserRequestDao {
	
	/**
     * Returns an element for the given Id.
     * 
     * @param newUserRequestId
     *            The id to query db
     * @return element
     */
    public NewUserRequest findById(UUID newUserRequestId) {
		try {
			return this.entityManager.createQuery("SELECT request from NewUserRequest request where request.newUserRequestId = :id AND :now < request.expireOn AND request.active = true", NewUserRequest.class)
					.setParameter("id", newUserRequestId)
					.setParameter("now", LocalDateTime.now())
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no user if found
			return null;
		}
	}
    
    /**
     * Returns an element for the given email and role.
     * 
     * @param email
     *            The email to query db
     * @param role
     *            The role to query db
     * @return element
     */
    public NewUserRequest findByEmailAndRole(String email, String role) {
		try {
			return this.entityManager.createQuery("SELECT request from NewUserRequest request where request.email = :email AND request.role = :role AND :now < request.expireOn AND request.active = true", NewUserRequest.class)
					.setParameter("email", email)
					.setParameter("role", role)
					.setParameter("now", LocalDateTime.now())
					.getSingleResult();
		}
		catch(NoResultException e) {
			// returns null if no user if found
			return null;
		}
	}
	
	/**
     * Returns if email exists in table
     * 
     * @param email
     * 			The email to check
     * @return boolean
     */
	public boolean emailExists(String email) {
		return this.entityManager.createQuery("SELECT COUNT(request) from NewUserRequest request where request.email = :email AND :now < request.expireOn AND request.active = true", Long.class)
				.setParameter("email", email)
				.setParameter("now", LocalDateTime.now())
				.getSingleResult() != 0;
	}
}
