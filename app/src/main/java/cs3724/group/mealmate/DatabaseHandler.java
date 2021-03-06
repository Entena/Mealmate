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
 * This class creates a user info DB and corresponding tables if one doesn't exist. Also allows
 * for easy insertion of records into the user info tables. Also easily get user info.
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
    private static final String GOAL_TIME = "goal_time";
    private static final String AGE = "age";
    private static final String SEX = "sex";
    private static final String HEIGHT = "height";
    private static final String WEIGHT = "weight";
    private static final String REMINDER = "reminder";
    private static final String PEBBLE = "pebble";

    private static String PATH;

    //SQLite DB
    SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        PATH = context.getFilesDir().getPath()+"/databases/UserInfo.db";
    }


    public void createDB() {
        Log.e("TESTING", "onCreate");

        db = this.getWritableDatabase();

        try {
        String CREATE_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DATE + " TEXT,"
                + TIME + " TEXT," + FOOD_ID + " INTEGER)";
        db.execSQL(CREATE_HISTORY_TABLE);

        String CREATE_GOALS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GOALS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + GOAL_ITEM + " TEXT,"
                + GOAL_METRIC + " TEXT," + GOAL_TIME + " TEXT)";
        db.execSQL(CREATE_GOALS_TABLE);
        String CREATE_SCHEDULE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SCHEDULE + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DATE + " TEXT,"
                + TIME + " TEXT," + FOOD_ID + " INTEGER)";
        db.execSQL(CREATE_SCHEDULE_TABLE);
        String CREATE_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTINGS + "("
                + ID + " INTEGER PRIMARY KEY," + AGE + " TEXT,"
                + SEX + " TEXT," + HEIGHT + " TEXT," + WEIGHT + " TEXT," + REMINDER + " TEXT,"
                + PEBBLE + " TEXT)";
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
       // db.execSQL("INSERT INTO " + TABLE_HISTORY + "(" + DATE + ", " + TIME + ", " + FOOD_ID + ") " +
          //      "VALUES ('" + item.getDate() + "', '" + item.getTime() + "', '" + item.getFoodID() + "');");
        db.insert(TABLE_HISTORY, null, values);
        //db.close(); // Closing database connection
    }

    public void addScheduleItem(CalendarFoodItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, item.getDate());
        values.put(TIME, item.getTime());
        values.put(FOOD_ID, item.getFoodID());

        // Inserting Row
        db.insert(TABLE_SCHEDULE, null, values);
        //db.close(); // Closing database connection
    }

    public void addSetting(Setting set) {
        //db = this.getWritableDatabase();
        //db = SQLiteDatabase.openDatabase(PATH, null, 0);
        if (db == null) {
            Log.e("TESTING", "DB Null in add setting");
        }
        ContentValues values = new ContentValues();
        values.put(ID, "1");
        values.put(AGE, set.getAge());
        values.put(SEX, set.getSex());
        values.put(HEIGHT, set.getHeight());
        values.put(WEIGHT, set.getWeight());
        //System.out.println("TEST " + set.getReminderTime());
        values.put(REMINDER, set.getReminderTime());
        String pebCon;
        if (set.usePebble()) {
            pebCon = "true";
        } else {
            pebCon = "false";
        }
        values.put(PEBBLE, pebCon);

        // Inserting Row
        db.insertWithOnConflict(TABLE_SETTINGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void addGoal(Goal goal) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GOAL_ITEM, goal.getGoalItem());
        values.put(GOAL_METRIC, goal.getGoalMetric());
        values.put(GOAL_TIME, goal.getGoalTime());

        // Inserting Row
        db.insert(TABLE_GOALS, null, values);
        //db.close(); // Closing database connection
    }

    public ArrayList<CalendarFoodItem> getHistory() {
        ArrayList<CalendarFoodItem> history = new ArrayList<>();
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_HISTORY, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            CalendarFoodItem cfi = new CalendarFoodItem(c.getString(1), c.getString(2), c.getString(3));
            cfi.setID(c.getString(0));
            history.add(cfi);

            c.moveToNext();
        }
        //c.close();
        //db.close();
        return history;
    }

    public ArrayList<CalendarFoodItem> getHistory(String date) {
        ArrayList<CalendarFoodItem> history = new ArrayList<>();
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_HISTORY + " WHERE " + DATE + " = ?; ", new String[] {date});
        c.moveToFirst();
        while (!c.isAfterLast()) {
            CalendarFoodItem cfi = new CalendarFoodItem(c.getString(1), c.getString(2), c.getString(3));
            cfi.setID(c.getString(0));
            history.add(cfi);
            c.moveToNext();
        }
        //c.close();
        //db.close();
        return history;
    }

    public ArrayList<CalendarFoodItem> getHistory(ArrayList<String> dates) {
        ArrayList<CalendarFoodItem> history = new ArrayList<>();
        //SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_HISTORY + " WHERE");
        for (int i = 0; i < dates.size(); i++) {
            if(dates.size() == 1) {
                query.append(" " + DATE + " LIKE '%" + dates.get(i) + "%'");
            } else {
                if (i == 0) {
                    query.append(" (" + DATE + " LIKE '%" + dates.get(i) + "%' OR");
                } else if (i == (dates.size() - 1)) {
                    query.append(" " + DATE + " LIKE '%" + dates.get(i) + "%')");
                } else {
                    query.append(" " + DATE + " LIKE '%" + dates.get(i) + "%' OR");
                }
            }

        }
        System.out.println(query.toString());
        Cursor c = db.rawQuery(query.toString(), null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            CalendarFoodItem cfi = new CalendarFoodItem(c.getString(1), c.getString(2), c.getString(3));
            cfi.setID(c.getString(0));
            history.add(cfi);
            c.moveToNext();
        }
        //c.close();
        //db.close();
        return history;
    }

    public ArrayList<CalendarFoodItem> getSchedule() {
        ArrayList<CalendarFoodItem> schedule = new ArrayList<>();
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            CalendarFoodItem cfi = new CalendarFoodItem(c.getString(1), c.getString(2), c.getString(3));
            cfi.setID(c.getString(0));
            schedule.add(cfi);
            c.moveToNext();
        }
        //c.close();
        //db.close();
        return schedule;
    }

    public ArrayList<CalendarFoodItem> getSchedule(String date) {
        ArrayList<CalendarFoodItem> schedule = new ArrayList<>();
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE + " WHERE " + DATE + " = ?; ", new String[] {date});
        c.moveToFirst();
        while (!c.isAfterLast()) {
            CalendarFoodItem cfi = new CalendarFoodItem(c.getString(1), c.getString(2), c.getString(3));
            cfi.setID(c.getString(0));
            schedule.add(cfi);
            c.moveToNext();
        }
       // c.close();
        //db.close();
        return schedule;
    }

    public ArrayList<CalendarFoodItem> getSchedule(ArrayList<String> dates) {
        ArrayList<CalendarFoodItem> schedule = new ArrayList<>();
        //SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_SCHEDULE + " WHERE");
        for (int i = 0; i < dates.size(); i++) {
            if(dates.size() == 1) {
                query.append(" " + DATE + " LIKE '%" + dates.get(i) + "%'");
            } else {
                if (i == 0) {
                    query.append(" (" + DATE + " LIKE '%" + dates.get(i) + "%' OR");
                } else if (i == (dates.size() - 1)) {
                    query.append(" " + DATE + " LIKE '%" + dates.get(i) + "%')");
                } else {
                    query.append(" " + DATE + " LIKE '%" + dates.get(i) + "%' OR");
                }
            }

        }
        Cursor c = db.rawQuery(query.toString(), null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            CalendarFoodItem cfi = new CalendarFoodItem(c.getString(1), c.getString(2), c.getString(3));
            cfi.setID(c.getString(0));
            schedule.add(cfi);
            c.moveToNext();
        }
        //c.close();
        //db.close();
        return schedule;
    }

    public ArrayList<Goal> getGoals() {
        ArrayList<Goal> goals = new ArrayList<>();
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_GOALS, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Goal g = new Goal(c.getString(1), c.getString(2), c.getString(3));
            g.setID(c.getString(0));
            goals.add(g);
            c.moveToNext();
        }
        //c.close();
        //db.close();
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
            boolean pebCon;
            if(c.getString(6) != null && c.getString(6).equals("true")) {
                pebCon = true;
            } else {
                pebCon = false;
            }
            setting = new Setting(c.getString(1), c.getString(2), c.getString(3),
                    c.getString(4), c.getString(5), pebCon);
            //System.out.println("TEST " + setting.getReminderTime());
            break;
        }
        //c.close();
        //db.close();
        return setting;
    }

    public void removeHistItem(String id) {
        db.execSQL("DELETE FROM " + TABLE_HISTORY + " WHERE " + ID + "='" + Integer.parseInt(id) + "';");
    }

    public void removeSchedItem(String id) {
        db.execSQL("DELETE FROM " + TABLE_SCHEDULE + " WHERE " + ID + "='" + Integer.parseInt(id) + "';");
    }

    public void removeGoal(String id) {
        db.execSQL("DELETE FROM " + TABLE_GOALS + " WHERE " + ID + "='" + Integer.parseInt(id) + "';");
    }
}
