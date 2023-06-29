package pushpender.com.streetlightcomplaints;

/**
 * Created by Pushpender Bhandari on 4/29/2016.
 */
public class Update {
    String Column1;
    String Column2;

    public Update(String column1, String column2) {
        this.Column1 = column1;
        this.Column2 = column2;
    }

    public Update() {
        this.Column1 = null;
        this.Column2 = null;

    }

    public String getColumn1() {
        return Column1;
    }

    public String setColumn1(String column1) {
        Column1 = column1;
        return column1;
    }

    public String getColumn2() {
        return Column2;
    }

    public String setColumn2(String column2) {
        Column2 = column2;
        return column2;
    }
}
