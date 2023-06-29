package pushpender.com.streetlightcomplaints;

/**
 * Created by Pushpender Bhandari on 4/22/2016.
 */
public class UserData {
    int Respose;
    String Message, PersonName;

    public UserData(int response, String message, String personName) {
        this.Respose = response;
        this.Message = message;
        this.PersonName = personName;
    }

    public UserData() {
        this.Respose = 0;
        this.Message = null;
        this.PersonName = null;
    }

    public int getRespose() {
        return Respose;
    }

    public int setRespose(int respose) {
        Respose = respose;
        return respose;
    }

    public String getMessage() {
        return Message;
    }

    public String setMessage(String message) {
        Message = message;
        return message;
    }

    public String getPersonName() {
        return PersonName;
    }

    public String setPersonName(String personName) {
        PersonName = personName;
        return personName;
    }
}
