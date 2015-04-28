package cs3724.group.mealmate;

/**
 * Created by Pat on 4/26/2015.
 * No setters needed because a new setting will replace the old setting in the DB
 */
public class Setting {

    private String age;
    private String sex;
    private String height;
    private String weight;
    private String reminderTime;
    private boolean pebbleConnected;

    public Setting(String age, String sex, String height, String weight, String reminderTime, boolean pebbleConnected) {
        this.age = age;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.reminderTime = reminderTime;
        this.pebbleConnected = pebbleConnected;
    }

    public String getAge() {
        return age;
    }
    public String getSex() {
        return sex;
    }
    public String getHeight() {
        return height;
    }
    public String getWeight() {
        return weight;
    }
    public String getReminderTime() { return reminderTime; }
    public boolean isPebbleConnected() {return pebbleConnected; }

    public void setPebbleConnected(boolean pebCon) {
        pebbleConnected = pebCon;
    }
}
