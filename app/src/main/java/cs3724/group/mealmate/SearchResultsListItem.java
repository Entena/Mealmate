package cs3724.group.mealmate;

public class SearchResultsListItem {
    public String name, cal, protein, carbs, fat, fiber, sodium, diningHall, foodID;

    public SearchResultsListItem(String name, String diningHall, String cal, String carbs,
                                 String protein, String fat, String fiber, String sodium, String foodID) {
        this.name = name;
        this.diningHall = diningHall;
        this.cal = cal;
        this.protein = protein;
        this.fat = fat;
        this.fiber = fiber;
        this.sodium = sodium;
        this.foodID = foodID;
        this.carbs = carbs;
    }

    public String toString() {
        return name + "\n" + diningHall + "\n" + "Cal:" + cal + " Cb:" + carbs + " P:" + protein +
                " Ft:" + fat + " Fb:" + fiber + " S:" + sodium;
    }

    public boolean equals(Object obj) {
        if (obj instanceof SearchResultsListItem) {
            if (((SearchResultsListItem) obj).foodID.equals(foodID)) {
                return true;
            }
        }
        return false;
    }
}