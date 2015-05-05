package cs3724.group.mealmate;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity implements ScheduleMealCandidateFragment.EventListener {
    // Fragment tags
    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";

    // local variables
    RetainedFragment retainedFragment;
    Notification not;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up retained fragment if not already set up
        if (getFragmentManager().findFragmentByTag(FRAG_RETAIN_TAG) == null) {
            // create fragment
            retainedFragment = new RetainedFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager()
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

        MainMenuFragment mmf = new MainMenuFragment();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainScrollView, mmf, "MAINMENU");
        fragmentTransaction.commit();

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
        if (id == R.id.action_main_menu) {
            MainMenuFragment mmf = new MainMenuFragment();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainScrollView, mmf, "MAINMENU");
            fragmentTransaction.commit();
            return true;
        }

        if (id == R.id.action_about) {
            AboutFragment af = new AboutFragment();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainScrollView, af, "ABOUT");
            fragmentTransaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        getFragmentManager().popBackStackImmediate();
    }

    /* Sends an alert to the Pebble that foods are to be eaten
     *
     * @param foods  A string list of foods scheduled for notification
     * @param diningCenters  A string list of dining centers where foods are scheduled
     */
    public void remindPebble(Notification not) {
        Log.e("Wes","remindPebble");
        // Send alert
        final Intent intent = new Intent("com.getpebble.action.SEND_NOTIFICATION");
        final Map data = new HashMap();
        data.put("title", "MealMate");
        data.put("body", not.reminder());
        final JSONObject jsonData = new JSONObject(data);
        final String notificationData = new JSONArray().put(jsonData).toString();

        intent.putExtra("messageType", "PEBBLE_ALERT");
        intent.putExtra("sender", "MealMate");
        intent.putExtra("notificationData", notificationData);

        sendBroadcast(intent);
    }

    @Override
    public void sendToPebble(Notification not) {
        Log.e("Test", "SendToPebble");
        this.not = not;
        remindPebble(not);

        NotificationCompat.Builder builder= new NotificationCompat.Builder(MainActivity.this);
        builder.setContentTitle("MealMate Reminder");
        builder.setContentText(not.reminder());
        builder.setSmallIcon(R.drawable.logo);
        builder.setTicker("MealMate scheduled meal");
        builder.setAutoCancel(true); //automatically cancel notification when selected

        //Intent
        Intent i = new Intent(MainActivity.this, Notification.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
        stackBuilder.addParentStack(Notification.class);
        stackBuilder.addNextIntent(i);
        PendingIntent pi_main = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pi_main);
        android.app.Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        Log.e("Wes", "Notify Called");
        manager.notify(1234, notification);
    }
}
