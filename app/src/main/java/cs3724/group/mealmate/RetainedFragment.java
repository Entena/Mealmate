package cs3724.group.mealmate;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Pat on 4/28/2015.
 */
public class RetainedFragment extends Fragment {
    SQLiteHelper foodDB;          // the food DB
    DatabaseHandler userInfoDB;     // the user information DB
    MMResultSet resultSet;               // store a result set for use across screens
    private String mode;

    public RetainedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Save instances when configuration changes
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setFoodDB(SQLiteHelper db) {
        foodDB = db;
    }

    public void setUserInfoDB(DatabaseHandler db)  {
        userInfoDB = db;
    }

    public void setResultSet(MMResultSet rs) {
        resultSet = rs;
    }

    public SQLiteHelper getFoodDB() {
        return foodDB;
    }

    public DatabaseHandler getUserInfoDB() {
        return userInfoDB;
    }

    public MMResultSet getLastResultSet() {
        return resultSet;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
