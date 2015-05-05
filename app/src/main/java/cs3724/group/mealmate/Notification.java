package cs3724.group.mealmate;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getpebble.android.kit.PebbleKit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wes on 5/5/15.
 */
public class Notification extends Activity implements ScheduleMealCandidateFragment.EventListener{
    private static final String TITLE = "Reminder:";
    private static final String MEAL_SCHED = "Scheduled Meal(s}";
    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";
    private final static String HISTORY_MODE = "history";
    private final static String SCHEDULE_MODE = "schedule";
    private StringBuilder builder;

    long dif;
    boolean usePebble;
    String scheduledTime;
    int remindTime;
    RetainedFragment retainedFragment;
    ArrayList<String> foodAndLocs;
    Notification not;

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

        ScheduleViewFragment mmf = new ScheduleViewFragment();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainScrollView, mmf, "SCHEDULEVIEWFRAGMENT");
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
        Log.e("Wes", "remindPebble");
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
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Log.e("Wes" , String.valueOf(not.getDif()));
//        am.set(AlarmManager.RTC_WAKEUP, not.getDif(), PendingIntent.getBroadcast(this, 1,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT));
        NotificationCompat.Builder builder= new NotificationCompat.Builder(Notification.this);
        builder.setContentTitle("MealMate Reminder");
        builder.setContentText(not.reminder());
        builder.setSmallIcon(R.drawable.logo);
        builder.setTicker("MealMate: Meal Scheduled");
        builder.setAutoCancel(true); //automatically cancel notification when selected

        //Intent
        Intent i = new Intent(Notification.this, Notification.class);

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

    class AlarmReceiver extends BroadcastReceiver {
        private final String TAG = AlarmReceiver.class.getName();
        private static final String SHOW_ID = "show_id";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Test", "onReceive");
            if (retainedFragment.getUserInfoDB().getUserSetting().usePebble() && PebbleKit.isWatchConnected(getApplicationContext())) {

                remindPebble(not);
            }
            //showNormalNotification();
            Bundle bundle = intent.getExtras();
            int showId = bundle.getInt(SHOW_ID);

        }
    }

    public Notification(){
        //base constructor
    }

    public Notification(DatabaseHandler userInfodb, String date, String timesched, ArrayList<String> foodAndLoc){
        remindTime = Integer.parseInt(userInfodb.getUserSetting().getReminderTime());
        foodAndLocs = foodAndLoc;
        usePebble = userInfodb.getUserSetting().usePebble();
        scheduledTime = timesched;
        String[] dateAr = date.split("/");
        int dayA = Integer.parseInt(dateAr[1]);
        int monthA = Integer.parseInt(dateAr[0]) - 1;
        int yearA = Integer.parseInt(dateAr[2]);
        String[] timeAr = timesched.split(":");
        String[] timeArMin = timeAr[1].split(" ");
        int hourA;
        if (timeArMin[1].equals("AM")) {
            hourA = Integer.parseInt(timeAr[0]);
        } else {
            hourA = (Integer.parseInt(timeAr[0]) + 12) % 24;
        }
        int minA = Integer.parseInt(timeArMin[0]);
        Calendar aCal = Calendar.getInstance();
        aCal.set(yearA, monthA, dayA, hourA, minA);
        Log.e("WEs", yearA + " " + monthA + " " + dayA + " " + hourA + " " + minA);
        Calendar cur = Calendar.getInstance();
        dif = (aCal.getTimeInMillis() - cur.getTimeInMillis() - remindTime);
        Log.e("Time Span", String.valueOf(dif));

    }


    public String reminder(){
        StringBuilder builder = new StringBuilder();
        builder.append(TITLE + "\n" + MEAL_SCHED + "\n@" + scheduledTime +":\n");
        for (int i = 0; i < foodAndLocs.size(); i++) {
            if (i == (foodAndLocs.size() - 1)){
                builder.append(foodAndLocs.get(i));
            } else {
                builder.append(foodAndLocs.get(i) + "\n");
            }
        }
        return builder.toString();
    }

    public long getDif() {
        return dif;
    }
}

