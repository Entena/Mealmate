package cs3724.group.mealmate;

/**
 * Created by Pat on 4/26/2015.
 * Class is used to store history and schedule records since they have the same fields
 */
public class CalendarFoodItem {

    private String id;
    private String date;
    private String time;
    private String food_id;

    public CalendarFoodItem(String date, String time, String food_id) {
        this.date = date;
        this.time = time;
        this.food_id = food_id;
    }
    public void setID(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getFoodID() {
        return food_id;
    }
    public String getID() { return id; }
}
