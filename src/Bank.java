import java.util.ArrayList;
import java.util.Random;

public class Bank {

    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;

    public Bank(String name) {
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }

    public String getNewUserUUID() {
        StringBuilder uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique;

        do {
            uuid = new StringBuilder();
            for (int i = 0; i < len; i++) {
                uuid.append(((Integer) rng.nextInt(10)));
            }
            nonUnique = false;
            for (User u : this.users) {
                if (uuid.toString().compareTo(u.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);

        return uuid.toString();
    }

    public String getNewAccountUUID() {
        StringBuilder uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique;

        do {
            uuid = new StringBuilder();
            for (int i = 0; i < len; i++) {
                uuid.append(((Integer) rng.nextInt(10)));
            }
            nonUnique = false;
            for (Account a : this.accounts) {
                if (uuid.toString().compareTo(a.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);

        return uuid.toString();
    }

    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }

    public User addUser(String firstName, String lastName, String pin) {
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);

        Account newAccount = new Account("Visa", newUser, this);
        newUser.addAccount(newAccount);
        this.accounts.add(newAccount);

        return newUser;
    }

    public User userLogin(String userID, String pin) {
        for (User u : this.users) {
            if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
                return u;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
