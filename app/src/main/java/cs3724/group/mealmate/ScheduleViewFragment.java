package cs3724.group.mealmate;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleViewFragment extends Fragment {
    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";

    private ArrayList<HistoryScheduleDisplayItem> selectedMeals;

    private CustomAdapter adapter;

    // retained fragment
    private RetainedFragment retainFrag;
    // DBs
    private SQLiteHelper foodDB;
    private DatabaseHandler userInfoDB;
    // UI Elements
    private ListView resultsTbl;
    private Button btnAddMeal;
    private Button btnRemove;
    private Spinner spinnerDuration;

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
        populateUI();
        return view;
    }

    private void setGlobals(View view) {
        btnAddMeal = (Button) view.findViewById(R.id.btnScheduleAddMeal);
        btnRemove = (Button) view.findViewById(R.id.btnScheduleRemove);
        spinnerDuration = (Spinner) view.findViewById(R.id.spinnerScheduleDuration);
        resultsTbl = (ListView) view.findViewById(R.id.scheduleTable);
        retainFrag = (RetainedFragment) getFragmentManager().findFragmentByTag(FRAG_RETAIN_TAG);
        userInfoDB = retainFrag.getUserInfoDB();
        foodDB = retainFrag.getFoodDB();
        selectedMeals = new ArrayList<HistoryScheduleDisplayItem>();
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
    }

    private void populateUI() {
        int numDays;
        String duration = spinnerDuration.getSelectedItem().toString();
        //ArrayList<CalendarFoodItem> schedule;
        //String selDate = calPicker.getDate.toString();
        if (duration.equals("Day")) {
            numDays = 1;
        } else if (duration.equals("Week")) {
            numDays = 7;
        } else {
            numDays = 30;
        }
        if (numDays == 1) {
            //schedule = userInfoDB.getHistory(selDate);
        } else {
            /*ArrayList<String> dates = new ArrayList<>(30);
            int day = calPicker.getDay;
            int month = calPicker.getMonth;
            int year = calPicker.getYear;
            Calendar date = new GregorianCalendar(year, month, day);
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            for (int i = 0; i < numDays; i++) {
                date.add(Calendar.DAY_OF_MONTH, i);
                dates.add(df.format(date));
            }
            schedule = userInfoDB.getHistory(dates);
            */
        }
        ArrayList<CalendarFoodItem> schedule = userInfoDB.getSchedule();
        int count = 0;
        ArrayList<HistoryScheduleDisplayItem> food = new ArrayList<HistoryScheduleDisplayItem>(schedule.size());
        Log.e("HIST", "" + schedule.size());
        for (CalendarFoodItem cfi : schedule)

        {
            MMResultSet rs = foodDB.execQuery("SELECT * FROM foods WHERE _id=" + cfi.getFoodID());
            HistoryScheduleDisplayItem item = new HistoryScheduleDisplayItem(rs.getFoodName(),
                    rs.getDiningHall(), round(rs.getCalories()), round(rs.getCarbs()), round(rs.getProtein()),
                    round(rs.getFat()), round(rs.getFiber()), round(rs.getSodium()),
                    cfi.getDate(), cfi.getTime(), cfi.getID());
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
}
