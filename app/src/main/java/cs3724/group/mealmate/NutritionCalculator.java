package cs3724.group.mealmate;

/**
 * Created by Pat on 4/29/2015.
 * This class calculates recommended values for the different nutritional items
 */
public class NutritionCalculator {
    // constants
    private static final double RECOMMENDED_SODIUM = 2300.0;
    private static final double PRO_PERCENT = .03;
    private static final double FAT_PERCENT = .0325;
    private static final double FIB_PERCENT = .014;
    private static final double ACTIVITY_MULT = 1.5;
    private static final double MEAL_DIVIDER = 3.5;

    // nutritional items
    int cals;
    int protein;
    int fat;
    int fiber;
    int sodium;

    public NutritionCalculator(Setting set) {
        calcNutrItems(set);
    }

    /*
    * These values are calculated according to the FDA's recommended values, daily caloric intake,
    * and BMR (Basal Metabolic Rate)
    */
    private void calcNutrItems(Setting setting) {
        double sexMult;
        double heightMult;
        double weightMult;
        double ageMult;

        if(setting.getSex().equals("Male")) {
            sexMult = 66;
            weightMult = 6.3;
            heightMult = 12.9;
            ageMult = 6.8;
        } else {
            sexMult = 655;
            weightMult = 4.3;
            heightMult = 4.7;
            ageMult = 4.7;
        }

        double weightDbl = Double.parseDouble(setting.getWeight());
        double heightDbl = Double.parseDouble(setting.getHeight());
        double ageDbl = Double.parseDouble(setting.getAge());

        double bmr = sexMult + (weightDbl * weightMult) + (heightDbl * heightMult) - (ageDbl * ageMult);
        double caloricIntake = bmr * ACTIVITY_MULT;

        protein = round((caloricIntake * PRO_PERCENT) / MEAL_DIVIDER);
        fat = round((caloricIntake * FAT_PERCENT) / MEAL_DIVIDER);
        fiber = round((caloricIntake * FIB_PERCENT) / MEAL_DIVIDER);
        sodium = round(RECOMMENDED_SODIUM / MEAL_DIVIDER);
        cals = round(caloricIntake / MEAL_DIVIDER);
    }

    public String getProtein() {
        return Integer.toString(protein);
    }

    public String getFat() {
        return Integer.toString(fat);
    }

    public String getFiber() {
        return Integer.toString(fiber);
    }

    public String getSodium() {
        return Integer.toString(sodium);
    }

    public String getCals() {
        return Integer.toString(cals);
    }

    private int round(double d) {
        return (int) Math.round(d);
    }
}
