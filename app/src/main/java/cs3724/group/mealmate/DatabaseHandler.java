package cs3724.group.mealmate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Pat on 4/26/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserInfo.db";

    // Table names
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_GOALS = "goals";
    private static final String TABLE_SCHEDULE = "schedule";
    private static final String TABLE_SETTINGS = "settings";

    // Table Columns names
    private static final String ID = "_id";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String FOOD_ID = "food_id";
    private static final String GOAL_ITEM = "goal_item";
    private static final String GOAL_METRIC = "goal_metric";
    private static final String AGE = "age";
    private static final String SEX = "sex";
    private static final String HEIGHT = "height";
    private static final String WEIGHT = "weight";
    private static final String REMINDER = "reminder";

    private static final String PATH = "/data/data/com.cs3714.edu.sqllitetest/databases/UserInfo.db";

    //SQLite DB
    SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void createDB() {
        Log.e("TESTING", "onCreate");

        db = this.getWritableDatabase();

        try {
        String CREATE_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + "("
                + ID + " INTEGER PRIMARY KEY," + DATE + " TEXT,"
                + TIME + " TEXT," + FOOD_ID + " INTEGER)";
        db.execSQL(CREATE_HISTORY_TABLE);

        String CREATE_GOALS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GOALS + "("
                + ID + " INTEGER PRIMARY KEY," + GOAL_ITEM + " TEXT,"
                + GOAL_METRIC + " TEXT)";
        db.execSQL(CREATE_GOALS_TABLE);
        String CREATE_SCHEDULE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SCHEDULE + "("
                + ID + " INTEGER PRIMARY KEY," + DATE + " TEXT,"
                + TIME + " TEXT," + FOOD_ID + " INTEGER)";
        db.execSQL(CREATE_SCHEDULE_TABLE);
        String CREATE_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTINGS + "("
                + ID + " INTEGER PRIMARY KEY," + AGE + " TEXT,"
                + SEX + " TEXT," + HEIGHT + " TEXT," + WEIGHT + " TEXT," + REMINDER + "TEXT)";
        db.execSQL(CREATE_SETTINGS_TABLE);
            Log.e("TESTING", "test");
        } catch (Exception e) {
            Log.e("TESTING", "Create Tables exception: " + e.toString());
        }
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);*/
    }

    public void addHistoryItem(CalendarFoodItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, item.getDate());
        values.put(TIME, item.getTime());
        values.put(FOOD_ID, item.getFoodID());

        // Inserting Row
        db.insert(TABLE_HISTORY, null, values);
        db.close(); // Closing database connection
    }

    public void addScheduleItem(CalendarFoodItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, item.getDate());
        values.put(TIME, item.getTime());
        values.put(FOOD_ID, item.getFoodID());

        // Inserting Row
        db.insert(TABLE_SCHEDULE, null, values);
        db.close(); // Closing database connection
    }

    public void addSetting(Setting set) {
        //db = this.getWritableDatabase();
        //db = SQLiteDatabase.openDatabase(PATH, null, 0);
        if (db == null) {
            Log.e("TESTING", "DB Null in add setting");
        }
        ContentValues values = new ContentValues();
        values.put(AGE, set.getAge());
        values.put(SEX, set.getSex());
        values.put(HEIGHT, set.getHeight());
        values.put(WEIGHT, set.getWeight());

        // Inserting Row
        db.insert(TABLE_SETTINGS, null, values);
    }

    public void addGoal(Goal goal) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GOAL_ITEM, goal.getGoalItem());
        values.put(GOAL_METRIC, goal.getGoalMetric());

        // Inserting Row
        db.insert(TABLE_GOALS, null, values);
        db.close(); // Closing database connection
    }

    public ArrayList<CalendarFoodItem> getHistory() {
        ArrayList<CalendarFoodItem> history = new ArrayList<>();
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM history", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            history.add(new CalendarFoodItem(c.getString(0), c.getString(1), c.getString(2)));
        }
        c.close();
        db.close();
        return history;
    }

    public ArrayList<CalendarFoodItem> getSchedule() {
        ArrayList<CalendarFoodItem> schedule = new ArrayList<>();
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM schedule", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            schedule.add(new CalendarFoodItem(c.getString(0), c.getString(1), c.getString(2)));
        }
        c.close();
        db.close();
        return schedule;
    }

    public ArrayList<Goal> getGoals() {
        ArrayList<Goal> goals = new ArrayList<>();
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM goals", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            goals.add(new Goal(c.getString(0), c.getString(1)));
        }
        c.close();
        db.close();
        return goals;
    }

    public Setting getUserSetting() {
        Setting setting = null;
        //SQLiteDatabase db = this.getWritableDatabase();
        //db = SQLiteDatabase.openDatabase(PATH, null, 0);
        if (db == null) {
            Log.e("TESTING", "DB Null in get setting");
        }
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_SETTINGS, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            setting = new Setting(c.getString(1), c.getString(2), c.getString(3), c.getString(4));
            break;
        }
        c.close();
        //db.close();
        return setting;
    }
}
