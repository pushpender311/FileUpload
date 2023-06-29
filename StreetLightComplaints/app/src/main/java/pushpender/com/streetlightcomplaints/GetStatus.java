package pushpender.com.streetlightcomplaints;

/**
 * Created by Pushpender Bhandari on 4/25/2016.
 */
public class GetStatus {
    String Description;
    int Figure;

    public GetStatus(String description, int figure) {
        this.Description = description;
        this.Figure = figure;
    }

    public GetStatus() {
        this.Description = null;

        this.Figure = 0;

    }

    public int getFigure() {
        return Figure;
    }

    public void setFigure(int figure) {
        Figure = figure;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
