package cs3724.group.mealmate;

import android.database.Cursor;

/**
 * Created by Pat on 4/26/2015.
 */
public class MMResultSet {
    // the cursor to the result set
    Cursor cursor;
    public MMResultSet(Cursor cursor) {
        this.cursor = cursor;
        this.cursor.moveToFirst();
    }

    public String toString() {
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public String getFoodID() {
        if(!cursor.isAfterLast()) {
            return cursor.getString(0);
        }
        return null;
    }

    public String getFoodName() {
        if(!cursor.isAfterLast()) {
            return cursor.getString(1);
        }
        return null;
    }

    public String getCalories() {
        if(!cursor.isAfterLast()) {
            return cursor.getString(4);
        }
        return null;
    }

    public String getProtein() {
        if(!cursor.isAfterLast()) {
            return cursor.getString(5);
        }
        return null;
    }

    public String getCarbs() {
        if(!cursor.isAfterLast()) {
            return cursor.getString(6);
        }
        return null;
    }

    public String getFat() {
        if(!cursor.isAfterLast()) {
            return cursor.getString(7);
        }
        return null;
    }

    public String getFiber() {
        if(!cursor.isAfterLast()) {
            return cursor.getString(9);
        }
        return null;
    }

    public String getSodium() {
        if(!cursor.isAfterLast()) {
            return cursor.getString(10);
        }
        return null;
    }

    public String getDiningHall() {
        if(!cursor.isAfterLast()) {
            return cursor.getString(11);
        }
        return null;
    }

    public void next() {
        if(!cursor.isAfterLast()) {
            cursor.moveToNext();
        }
    }

    public boolean hasNext() {
        return !cursor.isAfterLast();
    }

    public int getNumResults() {
        return cursor.getCount();
    }

    public void moveTo(int pos) {
        cursor.moveToPosition(pos);
    }
}
