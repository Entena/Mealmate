package cs3724.group.mealmate;

/**
 * Created by Pat on 4/26/2015.
 */
public class Setting {

    private String age;
    private String sex;
    private String height;
    private String weight;

    public Setting(String age, String sex, String height, String weight) {
        this.age = age;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
    }

    public String getAge() {
        return age;
    }
    public String getSex() {
        return sex;
    }
    public String getHeight() {
        return height;
    }
    public String getWeight() {
        return weight;
    }
}
