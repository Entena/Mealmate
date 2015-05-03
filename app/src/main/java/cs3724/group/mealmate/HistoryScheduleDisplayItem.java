package cs3724.group.mealmate;

/**
 * Created by Pat on 5/2/2015.
 */
public class HistoryScheduleDisplayItem {
    public String name, cal, carbs, protein, fat, fiber, sodium, diningHall, date, time, id;

    public HistoryScheduleDisplayItem(String name, String diningHall, String cal, String carbs, String protein, String fat,
                                      String fiber, String sodium, String date, String time, String id) {
        this.name = name;
        this.diningHall = diningHall;
        this.cal = cal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.fiber = fiber;
        this.sodium = sodium;
        this.date = date;
        this.time = time;
        this.id = id;
    }

    public String toString() {
        return name + "\n" + diningHall + "\n" + date + " @ " + time + "\n" +
                "Cal:" + cal + " Cb:" + carbs + " P:" + protein + " Ft:" + fat + " Fb:" + fiber +
                " S:" + sodium;
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
        if (obj instanceof HistoryScheduleDisplayItem) {
            if (((HistoryScheduleDisplayItem) obj).id.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
