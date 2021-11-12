package controllers.use_cases;
import controllers.use_cases.Database.OnDataReadListener;
import entities.Customer;
import entities.DeliveryMan;
import entities.User;
import org.apache.commons.codec.digest.DigestUtils;
import controllers.use_cases.Database.DBManager;

public abstract class UserManager extends DBManager<String, User> {

    String userType;

    public UserManager(String userType){
        this.userType = userType;
    }

    /**
     * Takes in a password entered and uses SHA256 to hash it
     * when storing in Firebase.
     *
     * @param password The password of User.
     * @return hashed password
     */
    public String createHash(String password){
        return DigestUtils.sha256Hex(password);
    }

    /**
     * A Factory to create the appropriate UserManager
     *
     * @param userType Type of User being created
     * @return Required user manager
     */
    public static UserManager getUserManager(String userType){

        if(userType.equalsIgnoreCase("CUSTOMER")){
            return new CustomerManager(userType);
        } else {
            return new DeliveryManManager(userType);
        }
    }

    /**
     * Creates a user as per the user type found
     *
     * All parameters are the arguments for creating DeliveryMan and Customer.
     * Any extraneous values entered not used by customer are ignored if userType
     * is customer.
     *
     * @return User created.
     */
    public User createUser(String n, int[] l, int num, String user, String pass, int sin, String transport, float rate){
        if(userType.equalsIgnoreCase("DELIVERYMAN")){
            return new DeliveryMan(n, l, num, user, pass, sin, transport, rate);
        }
        else{
            return new Customer(n, l, num, user, pass);
        }
    }

    /**
     * Register the user into database if possible
     * @return User if registered successfully, null if not so.
     */
    public User registration(String n, int[] l, int num, String user, String pass, int sin, String transport, float rate){
        User currUser = createUser(n, l, num, user, pass, sin, transport, rate);
        return discrepancyCheck(currUser) ? currUser : null; // Template of creating user and discrepancy check
    }

    protected abstract boolean discrepancyCheck(User currUser);

    /**
     * Returns the user if existing in database
     *
     * @param uname Username of user
     * @param password Password of user
     * @return The user if authenticated else null
     */
    public abstract void authenticate(String uname, String password, final OnDataReadListener onDataReadListener);

}