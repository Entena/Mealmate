package cs3724.group.mealmate;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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

    public MMResultSet search(String cal, String pro, String fat, String fib, String sod, ArrayList<String> halls) {
        StringBuilder query = new StringBuilder("SELECT * FROM foods WHERE");
        if (cal.length() != 0) {
            query.append(" CALS < " + cal + " AND");
        }
        if (pro.length() != 0) {
            query.append(" PROT > " + pro + " AND");
        }
        if (fat.length() != 0) {
            query.append(" FAT < " + fat + " AND");
        }
        if (fib.length() != 0) {
            query.append(" FIBER > " + fib + " AND");
        }
        if (sod.length() != 0) {
            query.append(" SOD < " + sod + " AND");
        }
        for (int i = 0; i < halls.size(); i++) {
            if(i == 0) {
                query.append(" (DINING_HALL_ID LIKE '%" + halls.get(i) + "%' OR");
            } else if (i == (halls.size() - 1)) {
                query.append(" DINING_HALL_ID LIKE '%" + halls.get(i) + "%')");
            } else {
                query.append(" DINING_HALL_ID LIKE '%" + halls.get(i) + "%' OR");
            }

        }
        String queryStr = query.toString();
        int size = queryStr.length();
        if (queryStr.endsWith("AND")) {
            queryStr = queryStr.substring(0, (size - 4));
        }
        MMResultSet rs = new MMResultSet(db.rawQuery(queryStr, null));
        return rs;
    }
}
