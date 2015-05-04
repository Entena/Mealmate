package cs3724.group.mealmate;

/**
 * Created by Pat on 5/2/2015.
 */
public class GoalDisplayItem {
    public String item, metric, time, id, acmp;

    public GoalDisplayItem(String item, String metric, String time, String id, String acmp) {
        this.item = item;
        this.metric = metric;
        this.time = time;
        this.id = id;
        this.acmp = acmp;
    }

    public String toString() {
        return (item +": " + acmp + "\n" + metric + " " + time);
    }

    public boolean equals(Object obj) {
        if (obj instanceof GoalDisplayItem) {
            if (((GoalDisplayItem) obj).id.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
