package pushpender.com.streetlightcomplaints;

public class UserAction {

    String action;

    public UserAction(String action) {
        super();
        this.action = action;
    }

    public UserAction() {
        super();
        this.action = null;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
