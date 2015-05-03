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
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryViewFragment extends Fragment {
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
    private Button btnGoals;
    private Button btnRemove;

    public HistoryViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_view, container, false);
        setGlobals(view);
        setListeners();
        return view;
    }

    private void setGlobals(View view) {
        btnAddMeal = (Button) view.findViewById(R.id.btnHistoryAddMeal);
        btnGoals = (Button) view.findViewById(R.id.btnHistoryGoals);
        btnRemove = (Button) view.findViewById(R.id.btnHistoryRemove);
        resultsTbl = (ListView) view.findViewById(R.id.historyTable);
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
        btnGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoalsViewFragment gvf = new GoalsViewFragment();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainScrollView, gvf, "GOALSVIEWFRAGMENT");
                fragmentTransaction.addToBackStack("GOALSVIEWFRAGMENT").commit();
            }
        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (HistoryScheduleDisplayItem item : selectedMeals) {
                    userInfoDB.removeHistItem(item.id);
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

        //MMResultSet rs = retainFrag.getLastResultSet();
        //MMResultSet rs = foodDB.execQuery("SELECT * FROM foods WHERE CALS < 854 AND PROT > 26 AND FAT < 28 AND FIBER > 12 AND SOD < 657 AND DINING_HALL_ID LIKE '%Owens%'");
        ArrayList<CalendarFoodItem> history = userInfoDB.getHistory();
        int count = 0;
        final ArrayList<HistoryScheduleDisplayItem> food = new ArrayList<HistoryScheduleDisplayItem>(history.size());
        Log.e("HIST", "" + history.size());
        for (
                CalendarFoodItem cfi
                : history)

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
