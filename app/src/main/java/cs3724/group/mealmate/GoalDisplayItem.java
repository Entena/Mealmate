package cs3724.group.mealmate;

/**
 * Created by Pat on 5/2/2015.
 */
public class GoalDisplayItem {
    public String item, metric, time, id;

    public GoalDisplayItem(String item, String metric, String time, String id) {
        this.item = item;
        this.metric = metric;
        this.time = time;
        this.id = id;
    }

    public String toString() {
        return (item +":\n" + metric + " " + time);
    }

    /*public boolean equals(Object obj) {
        if (obj instanceof HistoryScheduleDisplayItem) {
            if (((HistoryScheduleDisplayItem) obj).name.equals(name) &&
                    ((HistoryScheduleDisplayItem) obj).date.equals(date) &&
                    ((HistoryScheduleDisplayItem) obj).time.equals(time)) {
                return true;
            }
        }
        return false;
    }*/

    public boolean equals(Object obj) {
        if (obj instanceof GoalDisplayItem) {
            if (((GoalDisplayItem) obj).id.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
