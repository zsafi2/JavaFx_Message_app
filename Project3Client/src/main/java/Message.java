import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    static final long serialVersionUID = 42L;

    String Username;
    String message;
    String sendPerson;

    boolean login;
    boolean validName;
    boolean newUser;
    boolean userLeft;
    boolean messagetoAll;
    boolean personalMessage;

    ArrayList<String> PrevUsers;
}
