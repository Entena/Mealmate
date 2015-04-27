package cs3724.group.mealmate;

/**
 * Created by Pat on 4/26/2015.
 */
public class Goal {
    String goalItem;
    String goalMetric;

    public Goal(String goalItem, String goalMetric) {
        this.goalItem = goalItem;
        this.goalMetric = goalMetric;
    }

    public String getGoalItem() {
        return goalItem;
    }

    public String getGoalMetric() {
        return goalMetric;
    }
}
