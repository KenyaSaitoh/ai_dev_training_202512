package pro.kensait.berrybooks.dao;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pro.kensait.berrybooks.entity.Customer;

/**
 * CustomerDao - Data Access Object for Customer entity
 * 
 * Provides CRUD operations and authentication support for customers.
 */
@ApplicationScoped
public class CustomerDao {
    
    @PersistenceContext(unitName = "BerryBooksPU")
    private EntityManager em;
    
    /**
     * Find customer by ID
     * 
     * @param customerId the customer ID
     * @return the customer, or null if not found
     */
    public Customer findById(Integer customerId) {
        return em.find(Customer.class, customerId);
    }
    
    /**
     * Find customer by email (for login)
     * 
     * @param email the email address
     * @return the customer, or null if not found
     */
    public Customer findByEmail(String email) {
        try {
            TypedQuery<Customer> query = em.createQuery(
                "SELECT c FROM Customer c WHERE c.email = :email", Customer.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * Check if email already exists
     * 
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(c) FROM Customer c WHERE c.email = :email", Long.class);
        query.setParameter("email", email);
        Long count = query.getSingleResult();
        return count > 0;
    }
    
    /**
     * Find all customers
     * 
     * @return list of all customers
     */
    public List<Customer> findAll() {
        TypedQuery<Customer> query = em.createQuery(
            "SELECT c FROM Customer c ORDER BY c.customerId", Customer.class);
        return query.getResultList();
    }
    
    /**
     * Persist a new customer
     * 
     * @param customer the customer to persist
     */
    public void persist(Customer customer) {
        em.persist(customer);
    }
    
    /**
     * Update an existing customer
     * 
     * @param customer the customer to update
     * @return the updated customer
     */
    public Customer update(Customer customer) {
        return em.merge(customer);
    }
    
    /**
     * Remove a customer
     * 
     * @param customer the customer to remove
     */
    public void remove(Customer customer) {
        if (!em.contains(customer)) {
            customer = em.merge(customer);
        }
        em.remove(customer);
    }
}
