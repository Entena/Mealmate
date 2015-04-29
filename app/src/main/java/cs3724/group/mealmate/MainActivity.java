package cs3724.group.mealmate;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;


public class MainActivity extends Activity {
    // Fragment tags
    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";

    // local variables
    RetainedFragment retainedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainMenuFragment mmf = new MainMenuFragment();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainScrollView, mmf, "MAINMENU");
        fragmentTransaction.commit();

        // set up retained fragment if not already set up
        if (getFragmentManager().findFragmentByTag(FRAG_RETAIN_TAG) == null) {
            // create fragment
            retainedFragment = new RetainedFragment();
            fragmentTransaction = getFragmentManager()
                    .beginTransaction();
            // Add retained fragment to the activity, without a container,
            // meanwhile associate it with FRAG_RETAIN_TAG
            fragmentTransaction.add(retainedFragment, FRAG_RETAIN_TAG);
            fragmentTransaction.commit();

            // set up DBs
            DataBaseHelper myDbHelper = new DataBaseHelper(this);
            try {
                myDbHelper.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myDbHelper.openDataBase();
            SQLiteDatabase db = myDbHelper.getDB();
            SQLiteHelper foodDB = new SQLiteHelper(db);

            DatabaseHandler userInfoDB = new DatabaseHandler(this);
            userInfoDB.createDB();

            // add DBs to retained fragment
            retainedFragment.setFoodDB(foodDB);
            retainedFragment.setUserInfoDB(userInfoDB);
        }
        if (savedInstanceState != null) {
            // Every time during the recreate of the activity, the
            // retainedFragment will be lost, so we need to reassign the
            // retainedFragment
            retainedFragment = (RetainedFragment) getFragmentManager()
                    .findFragmentByTag(FRAG_RETAIN_TAG);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        getFragmentManager().popBackStackImmediate();
    }
}
