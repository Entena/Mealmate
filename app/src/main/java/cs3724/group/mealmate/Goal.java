package cs3724.group.mealmate;

/**
 * Created by Pat on 4/26/2015.
 */
public class Goal {
    String goalItem;
    String goalMetric;
    String goalTime;
    String id;

    public Goal(String goalItem, String goalMetric, String goalTime) {
        this.goalItem = goalItem;
        this.goalMetric = goalMetric;
        this.goalTime = goalTime;
    }

    public String getGoalItem() {
        return goalItem;
    }

    public String getGoalMetric() {
        return goalMetric;
    }

    public String getGoalTime() {return goalTime; }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }
}
