package cs3724.group.mealmate;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GoalsViewFragment extends Fragment {
    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";
    public final static String GOAL_MET = "Goal Met!";
    public final static String GOAL_NOT_MET = "Goal Not Met";

    private ArrayList<GoalDisplayItem> selectedMeals;

    private CustomAdapter adapter;
    //retained fragment
    private RetainedFragment retainFrag;
    // DBs
    private SQLiteHelper foodDB;
    private DatabaseHandler userInfoDB;

    //UI elements
    private Button btnAddGoal;
    private Button btnRemGoal;
    private TextView tvTotals;
    private ListView resultsTbl;
    private Spinner duration;

    public GoalsViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals_view, container, false);
        setUpGlobals(view);
        setUpListeners();
        //populateUI();
        return view;
    }

    private void setUpGlobals(View view){
        btnAddGoal = (Button) view.findViewById(R.id.btnGoalsAddGoal);
        tvTotals = (TextView) view.findViewById(R.id.textViewGoalsTotals);
        btnRemGoal = (Button) view.findViewById(R.id.btnGoalsRemove);
        resultsTbl = (ListView) view.findViewById(R.id.goalsTable);
        duration = (Spinner) view.findViewById(R.id.spinnerGoalDuration);
        retainFrag = (RetainedFragment) getFragmentManager().findFragmentByTag(FRAG_RETAIN_TAG);
        userInfoDB = retainFrag.getUserInfoDB();
        foodDB = retainFrag.getFoodDB();
        selectedMeals = new ArrayList<GoalDisplayItem>();
    }

    private void setUpListeners(){
        btnAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGoalViewFragment adgvf = new AddGoalViewFragment();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainScrollView, adgvf, "ADDGOALVIEWFRAGMENT");
                fragmentTransaction.addToBackStack("ADDGOALVIEWFRAGMENT").commit();
            }
        });
        btnRemGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (GoalDisplayItem item : selectedMeals) {
                    userInfoDB.removeGoal(item.id);
                    adapter.remove(item);
                }
                selectedMeals.clear();
                adapter.notifyDataSetChanged();

                for (int i = 0; i < resultsTbl.getCount(); i++) {
                    adapter.clearSelected();
                }
                adapter.notifyDataSetChanged();
            }
        });
        duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        ArrayList<CalendarFoodItem> history;
        if (duration.getSelectedItem().toString().equals("Past Week")) {
            numDays = 7;
        } else if (duration.getSelectedItem().toString().equals("Past Month")) {
            numDays = 30;
        } else {
            numDays = 1;
        }

        //get totals
        int cals = 0;
        int carbs = 0;
        int protein = 0;
        int fat = 0;
        int fiber = 0;
        int sodium = 0;
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String dateStr = df.format(today);
        if(duration.getSelectedItem().toString().equals("Past Day")) {
            //ArrayList<CalendarFoodItem> history = userInfoDB.getHistory(dateStr);
            history = userInfoDB.getHistory(dateStr);
        } else {
            ArrayList<String> dates = new ArrayList<>(30);
            //Toast.makeText(getActivity(), Integer.toString(month), Toast.LENGTH_SHORT).show();
            for (int i = 0; i < numDays; i++) {
                if(i != 0) {
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                }
                //Toast.makeText(getActivity(), df.format(cal.getTime()), Toast.LENGTH_SHORT).show();
                dates.add(df.format(cal.getTime()));
            }
            history = userInfoDB.getHistory(dates);
        }

        for(CalendarFoodItem cfi : history) {
            MMResultSet rs = foodDB.execQuery("SELECT * FROM foods WHERE _id=" + cfi.getFoodID());
            if (!rs.getCalories().equals("NULL")) {
                cals += round(rs.getCalories());
            }
            if (!rs.getCarbs().equals("NULL")) {
                carbs += round(rs.getCarbs());
            }
            if (!rs.getProtein().equals("NULL")) {
                protein += round(rs.getProtein());
            }
            if (!rs.getFat().equals("NULL")) {
                fat += round(rs.getFat());
            }
            if (!rs.getFiber().equals("NULL")) {
                fiber += round(rs.getFiber());
            }
            if (!rs.getSodium().equals("NULL")) {
                sodium += round(rs.getSodium());
            }
        }
        if (history.size() > 0) {
            tvTotals.setText("History Totals:\n" + "C:" + Integer.toString(cals) + " Cb:" +
                    Integer.toString(carbs) + " P:" + Integer.toString(protein) + " F:" +
                    Integer.toString(fat) + " Fb:" + Integer.toString(fiber) + " S:" +
                    Integer.toString(sodium));
        } else {
            tvTotals.setText("No History for this Period");
        }

        ArrayList<Goal> goals = userInfoDB.getGoals();
        int count = 0;
        ArrayList<GoalDisplayItem> food = new ArrayList<GoalDisplayItem>(goals.size());
        Log.e("HIST", "" + goals.size());
        for (Goal g : goals)
        {
            String dur = g.getGoalTime();
            String met = g.getGoalMetric();
            String[] metAr = met.split(" ");
            int metric = Integer.parseInt(metAr[2]);
            if(duration.getSelectedItem().toString().equals("Past Day")) {
                //System.out.println("DAY");
                if (g.goalTime.equals("per week")) {
                    int apx = (int) (Double.parseDouble(metAr[2]) / 7);
                    met = metAr[0] + " " + metAr[1] + " ~" + Integer.toString(apx);
                    dur = "per day";
                    metric = apx;
                }
                if (g.goalTime.equals("per month")) {
                    int apx = (int) (Double.parseDouble(metAr[2]) / 30);
                    met = metAr[0] + " " + metAr[1] + " ~" + Integer.toString(apx);
                    dur = "per day";
                    metric = apx;
                }
            }
            if(duration.getSelectedItem().toString().equals("Past Week")) {
                if (g.goalTime.equals("per day")) {
                    int apx = (int) (Double.parseDouble(metAr[2]) * 7);
                    met = metAr[0] + " " + metAr[1] + " ~" + Integer.toString(apx);
                    dur = "per week";
                    metric = apx;
                }
                if (g.goalTime.equals("per month")) {
                    int apx = (int) (Double.parseDouble(metAr[2]) / 4);
                    met = metAr[0] + " " + metAr[1] + " ~" + Integer.toString(apx);
                    dur = "per week";
                    metric = apx;
                }
            }
            if(duration.getSelectedItem().toString().equals("Past Month")) {
                if (g.goalTime.equals("per day")) {
                    int apx = (int) (Double.parseDouble(metAr[2]) * 30);
                    met = metAr[0] + " " + metAr[1] + " ~" + Integer.toString(apx);
                    dur = "per month";
                    metric = apx;
                }
                if (g.goalTime.equals("per week")) {
                    int apx = (int) (Double.parseDouble(metAr[2]) * 4);
                    met = metAr[0] + " " + metAr[1] + " ~" + Integer.toString(apx);
                    dur = "per month";
                    metric = apx;
                }
            }
            String op = metAr[0] + " " + metAr[1];
            String acmp = "";
            if(g.goalItem.equals("Calories")) {
                if(op.equals("At least")) {
                    if(cals >= metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                } else {
                    if(cals < metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                }
            } else if (g.goalItem.equals("Carbs")) {
                if(op.equals("At least")) {
                    if(carbs >= metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                } else {
                    if(carbs < metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                }
            } else if (g.goalItem.equals("Protein")) {
                if(op.equals("At least")) {
                    if(protein >= metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                } else {
                    if(protein < metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                }
            } else if (g.goalItem.equals("Fat")) {
                if(op.equals("At least")) {
                    if(fat >= metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                } else {
                    if(fat < metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                }
            } else if (g.goalItem.equals("Fiber")) {
                if(op.equals("At least")) {
                    if(fiber >= metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                } else {
                    if(fiber < metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                }
            } else if (g.goalItem.equals("Sodium")) {
                if(op.equals("At least")) {
                    if(sodium >= metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                } else {
                    if(sodium < metric) {
                        acmp = GOAL_MET;
                    } else {
                        acmp = GOAL_NOT_MET;
                    }
                }
            }
            GoalDisplayItem item = new GoalDisplayItem(g.goalItem, met,
                    dur, g.getID(), acmp);
            food.add(item);
        }

        adapter = new CustomAdapter(getActivity(), R.layout.ourlistitem, food);
        resultsTbl.setAdapter(adapter);
        resultsTbl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        resultsTbl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                  if (selectedMeals.contains((GoalDisplayItem) parent.getAdapter().getItem(position))) {
                                                      selectedMeals.remove((GoalDisplayItem) parent.getAdapter().getItem(position));
                                                      view.setBackgroundColor(view.getDrawingCacheBackgroundColor());
                                                      ((CustomAdapter) parent.getAdapter()).removeSelected(position);
                                                      Log.e("HIT", "VIEW REMOVING HIGH" + view.toString() + ":" + parent.getAdapter().getCount() + ":" + position);
                                                     // Log.e("HIT", "REMOVE HIGHLITE" + ((GoalDisplayItem) parent.getAdapter().getItem(position)).name);
                                                  } else {
                                                      ((CustomAdapter) parent.getAdapter()).setSelected(position);
                                                      selectedMeals.add((GoalDisplayItem) parent.getAdapter().getItem(position));
                                                      view.setBackgroundColor(getResources().getColor(R.color.highlighted_text_material_light));
                                                      Log.e("HIT", "VIEW BEING SET WITH HIGH" + view.toString() + ":" + parent.getAdapter().getCount() + ":" + position);
                                                      //Log.e("HIT", "ADD THE LIST/HIGHLIGHT" + ((HistoryScheduleDisplayItem) parent.getAdapter().getItem(position)).name);
                                                  }
                                              }
                                          }
        );
    }

    public class CustomAdapter extends ArrayAdapter<GoalDisplayItem> {
        protected static final int NO_SELECTED_COLOR = 0xFFFF;
        protected final int SELECTED_COLOR = getResources().getColor(R.color.highlighted_text_material_light);
        private ArrayList<GoalDisplayItem> items;
        private LayoutInflater mInflater;
        private int viewResourceId;
        private ArrayList<Integer> selectedPositions;

        public CustomAdapter(Activity activity, int resourceId, ArrayList<GoalDisplayItem> list) {
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

    private int round(String num) {
        Double d = Double.parseDouble(num);
        int round = (int) Math.round(d);
        return round;
    }
}
