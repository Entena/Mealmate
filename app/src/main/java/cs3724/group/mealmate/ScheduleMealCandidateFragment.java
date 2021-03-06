package cs3724.group.mealmate;


import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;


public class ScheduleMealCandidateFragment extends Fragment {
    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";
    private final static int MAX_ENTRIES = 1000;
    private final static String HISTORY_MODE = "history";
    private final static String SCHEDULE_MODE = "schedule";
    private final static String HUNGRY_MODE = "hungry";
    private final static int RANDOM_MAX = 5;

    private Calendar calendar;
    private DatePickerDialog dateDialog;
    private TimePickerDialog timeDialog;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    private ArrayList<SearchResultsListItem> selectedMeals;
    // retained fragment
    private RetainedFragment retainFrag;
    // DBs
    private DatabaseHandler userInfoDB;
    private SQLiteHelper foodDB;
    // UI Elements
    private ListView resultsTbl;
    private Button addButton;
    private EditText date;
    private EditText time;
    //event callback
    EventListener eventcallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_meal_candidate, container, false);
        setGlobals(view);
        setListeners();
        populateUI();
        return view;
    }

    private void setGlobals(View view) {
        resultsTbl = (ListView) view.findViewById(R.id.resultsTable);
        retainFrag = (RetainedFragment) getFragmentManager().findFragmentByTag(FRAG_RETAIN_TAG);
        userInfoDB = retainFrag.getUserInfoDB();
        foodDB = retainFrag.getFoodDB();
        selectedMeals = new ArrayList<SearchResultsListItem>();
        addButton = (Button) view.findViewById(R.id.btnScheduleMealCandidateAddMeal);

        date = (EditText) view.findViewById(R.id.datePicker);
        date.setInputType(InputType.TYPE_NULL);
        time = (EditText) view.findViewById(R.id.timePicker);
        time.setInputType(InputType.TYPE_NULL);

        //Set up calendar
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        timeFormat = new SimpleDateFormat("hh:mm a");
        setDateField();
        initDate();
        setTimeField();
        initTime();
    }

    private void setListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClick(v);
            }
        });
        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (v == date && e.getAction() == MotionEvent.ACTION_UP) {
                    dateDialog.show();
                    return true;
                }
                return false;
            }
        });
        time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (v == time && e.getAction() == MotionEvent.ACTION_UP) {
                    timeDialog.show();
                    return true;
                }
                return false;
            }
        });
    }

    public void populateUI() {
        MMResultSet rs = retainFrag.getLastResultSet();
        //MMResultSet rs = foodDB.execQuery("SELECT * FROM foods WHERE CALS < 2000 AND PROT > 0 AND FAT < 1000 AND FIBER > 0 AND SOD < 5000");
        //MMResultSet rs = foodDB.execQuery("SELECT * FROM foods WHERE CALS < 854 AND PROT > 26");
        int count = 0;
        final ArrayList<SearchResultsListItem> food = new ArrayList<SearchResultsListItem>(MAX_ENTRIES);
        if (retainFrag.getMode().equals(HUNGRY_MODE)) {
            int size = RANDOM_MAX;
            if (rs.getNumResults() < RANDOM_MAX) {
                size = rs.getNumResults();
            }
            int[] indexes = new int[size];
            for (int i = 0; i < size; i++) {
                indexes[i] = -1;
            }
            Random rand = new Random();
            for (int i = 0; i < size; i++) {
                boolean check;
                while (true) {
                    check = false;
                    int pos = rand.nextInt(rs.getNumResults());
                    for (int j = 0; j < i; j++) {
                        if (indexes[j] == pos) {
                            check = true;
                        }
                    }
                    if (!check) {
                        indexes[i] = pos;
                        break;
                    }
                    //System.out.println("TESTWHILE");
                }
            }
            for (int i = 0; i < size; i++) {
                rs.moveTo(indexes[i]);
                SearchResultsListItem item = new SearchResultsListItem(rs.getFoodName(), rs.getDiningHall(),
                        round(rs.getCalories()), round(rs.getCarbs()), round(rs.getProtein()), round(rs.getFat()),
                        round(rs.getFiber()), round(rs.getSodium()), round(rs.getFoodID()));
                //food.add(rs.getFoodName()+"\n"+rs.getDiningHall()+"\n"+"Cal:"+rs.getCalories()+" P:"+rs.getProtein()+" Ft:"+rs.getFat()+" Fb:"+rs.getFiber()+" S:"+rs.getSodium());
                food.add(item);
            }
        } else {
            while (rs.hasNext() && count < MAX_ENTRIES) {
                SearchResultsListItem item = new SearchResultsListItem(rs.getFoodName(), rs.getDiningHall(),
                        round(rs.getCalories()), round(rs.getCarbs()), round(rs.getProtein()), round(rs.getFat()),
                        round(rs.getFiber()), round(rs.getSodium()), round(rs.getFoodID()));
                //food.add(rs.getFoodName()+"\n"+rs.getDiningHall()+"\n"+"Cal:"+rs.getCalories()+" P:"+rs.getProtein()+" Ft:"+rs.getFat()+" Fb:"+rs.getFiber()+" S:"+rs.getSodium());
                food.add(item);
                rs.next();
                count++;
            }
        }
        CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.ourlistitem, food);
        resultsTbl.setAdapter(adapter);
        resultsTbl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        resultsTbl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedMeals.contains((SearchResultsListItem) parent.getAdapter().getItem(position))) {
                    selectedMeals.remove((SearchResultsListItem) parent.getAdapter().getItem(position));
                    view.setBackgroundColor(view.getDrawingCacheBackgroundColor());
                    ((CustomAdapter) parent.getAdapter()).removeSelected(position);
                    Log.e("HIT", "VIEW REMOVING HIGH" + view.toString() + ":" + parent.getAdapter().getCount() + ":" + position);
                    Log.e("HIT", "REMOVE HIGHLITE" + ((SearchResultsListItem) parent.getAdapter().getItem(position)).name);
                } else {
                    ((CustomAdapter) parent.getAdapter()).setSelected(position);
                    selectedMeals.add((SearchResultsListItem) parent.getAdapter().getItem(position));
                    view.setBackgroundColor(getResources().getColor(R.color.highlighted_text_material_light));
                    Log.e("HIT", "VIEW BEING SET WITH HIGH" + view.toString() + ":" + parent.getAdapter().getCount() + ":" + position);
                    Log.e("HIT", "ADD THE LIST/HIGHLIGHT" + ((SearchResultsListItem) parent.getAdapter().getItem(position)).name);
                }
            }
        });
    }

    public void OnClick(View v) {
        if (v.getId() == addButton.getId()) {
            if (selectedMeals.size() != 0) {
                Calendar cur = Calendar.getInstance();
                long curTime = cur.getTimeInMillis();
                long setTime = calendar.getTimeInMillis();
                if (retainFrag.getMode().equals(HISTORY_MODE)) {
                    if (setTime < curTime) {
                        for (SearchResultsListItem item : selectedMeals) {
                            CalendarFoodItem calItem = new CalendarFoodItem(date.getText().toString(), time.getText().toString(), item.foodID);
                            userInfoDB.addHistoryItem(calItem);
                        }
                        Toast.makeText(getActivity(), "Item(s) added to History", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Please set a date in the past", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (setTime >= curTime) {
                        ArrayList<String> notStrs = new ArrayList<>(selectedMeals.size());
                        for (SearchResultsListItem item : selectedMeals) {
                            CalendarFoodItem calItem = new CalendarFoodItem(date.getText().toString(), time.getText().toString(), item.foodID);
                            userInfoDB.addScheduleItem(calItem);
                            notStrs.add(item.name + "\n@" + item.diningHall);
                        }
                        Notification not = new Notification(userInfoDB, date.getText().toString(), time.getText().toString(), notStrs);
                        Toast.makeText(getActivity(), "Item(s) added to Schedule", Toast.LENGTH_SHORT).show();
                        eventcallback.sendToPebble(not);
                    } else {
                        Toast.makeText(getActivity(), "Please set a date in the future", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getActivity(), "Please select a food item", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class CustomAdapter extends ArrayAdapter<SearchResultsListItem> {
        protected static final int NO_SELECTED_COLOR = 0xFFFF;
        protected final int SELECTED_COLOR = getResources().getColor(R.color.highlighted_text_material_light);
        private ArrayList<SearchResultsListItem> items;
        private LayoutInflater mInflater;
        private int viewResourceId;
        private ArrayList<Integer> selectedPositions;

        public CustomAdapter(Activity activity, int resourceId, ArrayList<SearchResultsListItem> list) {
            super(activity, resourceId, list);
            // Sets the layout inflater
            mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Set a copy of the layout to inflate
            viewResourceId = resourceId;
            // Set a copy of the list
            items = list;
            selectedPositions = new ArrayList<Integer>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = (TextView) convertView;
            if (tv == null) {
                tv = (TextView) mInflater.inflate(viewResourceId, null);
            }
            tv.setText(items.get(position).toString());
            // Change the background color
            if (selectedPositions.contains(position)) {
                tv.setBackgroundColor(SELECTED_COLOR);
            } else {
                tv.setBackgroundColor(NO_SELECTED_COLOR);
            }
            return tv;
        }

        public void setSelected(int position) {
            //This is only for highlighting
            selectedPositions.add(position);
        }

        public void removeSelected(int position) {
            //This is only for highlighting
            selectedPositions.remove(new Integer(position));
        }
    }

    private String round(String num) {
        if (!num.equals("NULL")) {
            Double d = Double.parseDouble(num);
            int round = (int) Math.round(d);
            String rStr = Integer.toString(round);
            return rStr;
        } else {
            return "UNKN";
        }
    }

    private void setDateField() {
        dateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(year, monthOfYear, dayOfMonth);

                date.setText(dateFormat.format(calendar.getTime()));
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dateDialog.getDatePicker().setCalendarViewShown(true);
    }

    private void setTimeField() {
        if (retainFrag.getMode().equals(HISTORY_MODE)) {
            calendar.add(Calendar.HOUR_OF_DAY, -1);
        } else {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }
        timeDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hour, int minute) {
                /*hour = hour % 12;
                if (hour == 0) {
                    hour = 12;
                }*/
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                time.setText(timeFormat.format(calendar.getTime()));
            }

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
    }

    private void initDate() {
        date.setText(dateFormat.format(calendar.getTime()));
    }

    private void initTime() {
        time.setText(timeFormat.format(calendar.getTime()));
    }

    public interface EventListener {
        public void sendToPebble(Notification not);
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        eventcallback = (EventListener) activity;
    }
}
