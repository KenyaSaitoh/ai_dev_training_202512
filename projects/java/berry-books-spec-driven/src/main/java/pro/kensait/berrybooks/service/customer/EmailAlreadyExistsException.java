package pro.kensait.berrybooks.service.customer;

/**
 * EmailAlreadyExistsException
 * 
 * Thrown when attempting to register a customer with an email that already exists.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String email;
    
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
        this.email = email;
    }
    
    public EmailAlreadyExistsException(String email, String message) {
        super(message);
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
}
