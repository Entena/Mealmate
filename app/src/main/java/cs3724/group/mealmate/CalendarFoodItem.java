package cs3724.group.mealmate;

import java.util.Calendar;

/**
 * Created by Pat on 4/26/2015.
 * Class is used to store history and schedule records since they have the same fields
 */
public class CalendarFoodItem implements Comparable{

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

    @Override
    public int compareTo(Object another) {
        // get A calendar
        String[] dateAr = this.date.split("/");
        int dayA = Integer.parseInt(dateAr[1]);
        int monthA = Integer.parseInt(dateAr[0]) - 1;
        int yearA = Integer.parseInt(dateAr[2]);
        String[] timeAr = this.time.split(":");
        String[] timeArMin = timeAr[1].split(" ");
        int hourA;
        if (timeArMin[1].equals("AM")) {
            hourA = Integer.parseInt(timeAr[0]);
        } else {
            hourA = (Integer.parseInt(timeAr[0]) + 12) % 24;
        }
        int minA = Integer.parseInt(timeArMin[0]);
        Calendar aCal = Calendar.getInstance();
        aCal.set(yearA, monthA, dayA, hourA, minA);

        CalendarFoodItem b = (CalendarFoodItem) another;
        String[] dateArB = b.date.split("/");
        int day = Integer.parseInt(dateArB[1]);
        int month = Integer.parseInt(dateArB[0]) - 1;
        int year = Integer.parseInt(dateArB[2]);
        String[] timeArB = b.time.split(":");
        String[] timeArMinB = timeArB[1].split(" ");
        int hour;
        if (timeArMinB[1].equals("AM")) {
            hour = Integer.parseInt(timeArB[0]);
        } else {
            hour = (Integer.parseInt(timeArB[0]) + 12) % 24;
        }
        int min = Integer.parseInt(timeArMinB[0]);
        Calendar bCal = Calendar.getInstance();
        bCal.set(year, month, day, hour, min);

        int aTime = (int) aCal.getTimeInMillis();
        int bTime = (int) bCal.getTimeInMillis();
        int result = aTime - bTime;
        return result;
    }
}
