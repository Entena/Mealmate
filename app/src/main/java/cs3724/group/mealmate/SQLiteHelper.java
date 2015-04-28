package cs3724.group.mealmate;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Pat on 4/26/2015.
 * Use DataBaseHelper to get the foodDB from the assets folder and feed that DB into this
 * constructor. This class allows easy searching of the food DB
 */
public class SQLiteHelper {
    private static final String PATH = "main/assets/food.db";

    private SQLiteDatabase db;
    //private DBManager dbManager;

    public SQLiteHelper(SQLiteDatabase db) {
       this.db = db;

    }

    public MMResultSet execQuery(String query) {
        MMResultSet rs = new MMResultSet(db.rawQuery(query, null));
        return rs;
    }

    public void close() {
        db.close();
    }

    public MMResultSet search(String cal, String pro, String carb, String fat, String fib, String sod, String[] halls) {
        StringBuilder query = new StringBuilder("SELECT * FROM foods WHERE");
        if (cal != null) {
            query.append(" CALS < " + cal + " AND");
        }
        if (pro != null) {
            query.append(" PROT > " + pro + " AND");
        }
        if (carb != null) {
            query.append(" CARB < " + carb + " AND");
        }
        if (fat != null) {
            query.append(" FAT < " + fat + " AND");
        }
        if (fib != null) {
            query.append(" FIBER > " + fib + " AND");
        }
        if (sod != null) {
            query.append(" SOD < " + sod + "AND");
        }
        for (int i = 0; i < halls.length; i++) {
            if (halls[i] != null) {
                query.append(" DINING_HALL_ID LIKE %" + halls[i] + "% AND");
            }
        }
        String queryStr = query.toString();
        int size = queryStr.length();
        if(queryStr.endsWith("AND")) {
            queryStr.substring(0, (size - 4));
        }
        MMResultSet rs = new MMResultSet(db.rawQuery(queryStr, null));
        return rs;
    }
}
