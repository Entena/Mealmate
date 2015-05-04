package cs3724.group.mealmate;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class ScheduleViewFragment extends Fragment {
    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";

    private ArrayList<HistoryScheduleDisplayItem> selectedMeals;

    private CustomAdapter adapter;

    // retained fragment
    private RetainedFragment retainFrag;
    // DBs
    private SQLiteHelper foodDB;
    private DatabaseHandler userInfoDB;

    private Calendar calendar;
    private DatePickerDialog dateDialog;
    private SimpleDateFormat dateFormat;

    // UI Elements
    private ListView resultsTbl;
    private Button btnAddMeal;
    private Button btnRemove;
    private Button btnToHistory;
    private Spinner spinnerDuration;
    private EditText date;

    public ScheduleViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_view, container, false);
        setGlobals(view);
        setListeners();
        //populateUI();
        return view;
    }

    private void setGlobals(View view) {
        btnAddMeal = (Button) view.findViewById(R.id.btnScheduleAddMeal);
        btnRemove = (Button) view.findViewById(R.id.btnScheduleRemove);
        btnToHistory = (Button) view.findViewById(R.id.btnScheduleAddHistoryMeal);
        spinnerDuration = (Spinner) view.findViewById(R.id.spinnerScheduleDuration);
        resultsTbl = (ListView) view.findViewById(R.id.scheduleTable);
        retainFrag = (RetainedFragment) getFragmentManager().findFragmentByTag(FRAG_RETAIN_TAG);
        userInfoDB = retainFrag.getUserInfoDB();
        foodDB = retainFrag.getFoodDB();
        selectedMeals = new ArrayList<HistoryScheduleDisplayItem>();

        date = (EditText) view.findViewById(R.id.editTextScheduleDay);
        date.setInputType(InputType.TYPE_NULL);


        //Set up calendar
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        setDateField();
        initDate();
    }

    private void setListeners() {
        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleAddMealViewFragment sadmvf = new ScheduleAddMealViewFragment();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainScrollView, sadmvf, "SCHEDULEADDMEALVIEWWFRAGMENT");
                fragmentTransaction.addToBackStack("SCHEDULEADDMEALVIEWFRAGMENT").commit();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (HistoryScheduleDisplayItem item : selectedMeals) {
                    userInfoDB.removeSchedItem(item.id);
                    adapter.remove(item);
                }
                selectedMeals.clear();
                adapter.notifyDataSetChanged();
                //System.out.println(adapter.getCount());
                for (int i = 0; i < resultsTbl.getCount(); i++) {
                    //View tv = resultsTbl.getChildAt(i);
                    //View tv = adapter.getView(i, null, resultsTbl);
                    //tv.setBackgroundColor(Color.WHITE);//tv.getDrawingCacheBackgroundColor());
                    //adapter.notifyDataSetChanged();
                    adapter.clearSelected();
                }
                adapter.notifyDataSetChanged();
            }
        });
        btnToHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (HistoryScheduleDisplayItem item : selectedMeals) {
                    userInfoDB.removeSchedItem(item.id);
                    userInfoDB.addHistoryItem(new CalendarFoodItem(item.date, item.time, item.food_id));
                    adapter.remove(item);
                }
                selectedMeals.clear();
                adapter.notifyDataSetChanged();
                //System.out.println(adapter.getCount());
                for (int i = 0; i < resultsTbl.getCount(); i++) {
                    adapter.clearSelected();
                }
                adapter.notifyDataSetChanged();
            }
        });
        spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                populateUI();
                //System.out.println("TEST");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // nothing to do here
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
    }

    private void populateUI() {
        int numDays;
        String duration = spinnerDuration.getSelectedItem().toString();
        ArrayList<CalendarFoodItem> schedule;
        String selDate = date.getText().toString();
        if (duration.equals("Day")) {
            numDays = 1;
        } else if (duration.equals("Week")) {
            numDays = 7;
        } else {
            numDays = 30;
        }
        if (numDays == 1) {
            schedule = userInfoDB.getSchedule(selDate);
        } else {
            ArrayList<String> dates = new ArrayList<>(30);
            String[] dateAr = selDate.split("/");
            int day = Integer.parseInt(dateAr[1]);
            int month = Integer.parseInt(dateAr[0]) - 1;
            int year = Integer.parseInt(dateAr[2]);
            //Toast.makeText(getActivity(), Integer.toString(month), Toast.LENGTH_SHORT).show();
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            for (int i = 0; i < numDays; i++) {
                if(i != 0) {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }
                //Toast.makeText(getActivity(), df.format(cal.getTime()), Toast.LENGTH_SHORT).show();
                dates.add(df.format(cal.getTime()));
            }
            schedule = userInfoDB.getSchedule(dates);
        }
        //sort schedule
        Collections.sort(schedule);

        int count = 0;
        ArrayList<HistoryScheduleDisplayItem> food = new ArrayList<HistoryScheduleDisplayItem>(schedule.size());
        Log.e("HIST", "" + schedule.size());
        for (CalendarFoodItem cfi : schedule)

        {
            MMResultSet rs = foodDB.execQuery("SELECT * FROM foods WHERE _id=" + cfi.getFoodID());
            HistoryScheduleDisplayItem item = new HistoryScheduleDisplayItem(rs.getFoodName(),
                    rs.getDiningHall(), round(rs.getCalories()), round(rs.getCarbs()), round(rs.getProtein()),
                    round(rs.getFat()), round(rs.getFiber()), round(rs.getSodium()),
                    cfi.getDate(), cfi.getTime(), cfi.getID(), rs.getFoodID());
            //food.add(rs.getFoodName()+"\n"+rs.getDiningHall()+"\n"+"Cal:"+rs.getCalories()+" P:"+rs.getProtein()+" Ft:"+rs.getFat()+" Fb:"+rs.getFiber()+" S:"+rs.getSodium());
            food.add(item);
        }

        adapter = new CustomAdapter(getActivity(), R.layout.ourlistitem, food);
        resultsTbl.setAdapter(adapter);
        resultsTbl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        resultsTbl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                  if (selectedMeals.contains((HistoryScheduleDisplayItem) parent.getAdapter().getItem(position))) {
                                                      selectedMeals.remove((HistoryScheduleDisplayItem) parent.getAdapter().getItem(position));
                                                      view.setBackgroundColor(view.getDrawingCacheBackgroundColor());
                                                      ((CustomAdapter) parent.getAdapter()).removeSelected(position);
                                                      Log.e("HIT", "VIEW REMOVING HIGH" + view.toString() + ":" + parent.getAdapter().getCount() + ":" + position);
                                                      Log.e("HIT", "REMOVE HIGHLITE" + ((HistoryScheduleDisplayItem) parent.getAdapter().getItem(position)).name);
                                                  } else {
                                                      ((CustomAdapter) parent.getAdapter()).setSelected(position);
                                                      selectedMeals.add((HistoryScheduleDisplayItem) parent.getAdapter().getItem(position));
                                                      view.setBackgroundColor(getResources().getColor(R.color.highlighted_text_material_light));
                                                      Log.e("HIT", "VIEW BEING SET WITH HIGH" + view.toString() + ":" + parent.getAdapter().getCount() + ":" + position);
                                                      Log.e("HIT", "ADD THE LIST/HIGHLIGHT" + ((HistoryScheduleDisplayItem) parent.getAdapter().getItem(position)).name);
                                                  }
                                              }
                                          }
        );
    }

    public class CustomAdapter extends ArrayAdapter<HistoryScheduleDisplayItem> {
        protected static final int NO_SELECTED_COLOR = 0xFFFF;
        protected final int SELECTED_COLOR = getResources().getColor(R.color.highlighted_text_material_light);
        private ArrayList<HistoryScheduleDisplayItem> items;
        private LayoutInflater mInflater;
        private int viewResourceId;
        private ArrayList<Integer> selectedPositions;

        public CustomAdapter(Activity activity, int resourceId, ArrayList<HistoryScheduleDisplayItem> list) {
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

        public void clearSelected() {
            selectedPositions.clear();
        }
    }

    private String round(String num) {
        Double d = Double.parseDouble(num);
        int round = (int) Math.round(d);
        String rStr = Integer.toString(round);
        return rStr;
    }

    private void setDateField() {
        dateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(year, monthOfYear, dayOfMonth);

                date.setText(dateFormat.format(calendar.getTime()));
                populateUI();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dateDialog.getDatePicker().setCalendarViewShown(true);
    }

    private void initDate() {
        date.setText(dateFormat.format(calendar.getTime()));
    }
}
