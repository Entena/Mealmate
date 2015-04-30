package cs3724.group.mealmate;

import android.widget.CheckBox;

/**
 * Created by Pat on 4/30/2015.
 */
public class SearchItem {
    CheckBox cb;
    String foodID;
    String foodName;
    String cals;
    String protein;
    String fat;
    String fiber;
    String sodium;
    String dHall;

    public SearchItem(CheckBox cb, String foodID, String foodName, String cals, String protein, String fat,
            String fiber, String sodium, String dHall) {
        this.cb = cb;
        this.foodID = foodID;
        this.foodName = foodName;
        this.cals = cals;
        this.protein = protein;
        this.fat = fat;
        this.fiber = fiber;
        this.sodium = sodium;
        this.dHall = dHall;
    }

    public CheckBox getCb() {
        return cb;
    }

    public String getCals() {
        return cals;
    }

    public String getFiber() {
        return fiber;
    }

    public String getFat() {
        return fat;
    }

    public String getFoodID() {
        return foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getProtein() {
        return protein;
    }

    public String getSodium() {
        return sodium;
    }

    public String getDiningHall() {
        return dHall;
    }
}

