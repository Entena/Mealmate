package cs3724.group.mealmate;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ScheduleAddMealViewFragment extends Fragment {

    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";
    private final static String HISTORY_MODE = "history";
    private final static String SCHEDULE_MODE = "schedule";
    private final static String HUNGRY_MODE = "hungry";

    // retained fragment
    RetainedFragment retainFrag;

    // DBs
    DatabaseHandler userInfoDB;
    SQLiteHelper foodDB;

    // UI Elements
    EditText cals;
    EditText carbs;
    EditText protein;
    EditText fat;
    EditText fiber;
    EditText sodium;
    CheckBox owensHG;
    CheckBox b37;
    CheckBox d2dx;
    CheckBox turner;
    CheckBox westEnd;
    CheckBox deets;
    private Button btnAddMealGo;
    private Button btnAddMealMap;

    public ScheduleAddMealViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_add_meal_view, container, false);

        retainFrag = (RetainedFragment) getFragmentManager()
                .findFragmentByTag(FRAG_RETAIN_TAG);

        // get UI elements
        cals = (EditText) view.findViewById(R.id.scheduleAddMealEditCalories);
        carbs = (EditText) view.findViewById(R.id.scheduleAddMealEditCarbs);
        protein = (EditText) view.findViewById(R.id.scheduleAddMealEditProtein);
        fat = (EditText) view.findViewById(R.id.scheduleAddMealEditFat);
        fiber = (EditText) view.findViewById(R.id.scheduleAddMealEditFiber);
        sodium = (EditText) view.findViewById(R.id.scheduleAddMealEditSodium);
        owensHG = (CheckBox) view.findViewById(R.id.scheduleAddMealCheckBoxOwensHG);
        deets = (CheckBox) view.findViewById(R.id.scheduleAddMealCheckBoxDeets);
        b37 = (CheckBox) view.findViewById(R.id.scheduleAddMealCheckBoxB37);
        d2dx = (CheckBox) view.findViewById(R.id.scheduleAddMealCheckBoxD2DX);
        westEnd = (CheckBox) view.findViewById(R.id.scheduleAddMealCheckBoxWestEnd);
        turner = (CheckBox) view.findViewById(R.id.scheduleAddMealCheckBoxTurnerPlace);

        // get DBs
        userInfoDB = retainFrag.getUserInfoDB();
        foodDB = retainFrag.getFoodDB();
        populateUI();

        setGlobals(view);
        setListeners();
        return view;
    }

    private void setGlobals(View view) {
        btnAddMealGo = (Button) view.findViewById(R.id.btnScheduleAddMealGo);
        btnAddMealMap = (Button) view.findViewById(R.id.btnScheduleAddMealMap);
    }

    private void setListeners() {
        btnAddMealGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String calStr = cals.getText().toString();
                String carbStr = carbs.getText().toString();
                String proStr = protein.getText().toString();
                String fatStr = fat.getText().toString();
                String fibStr = fiber.getText().toString();
                String sodStr = sodium.getText().toString();

                if (calStr.length() == 0 || carbStr.length() == 0 || proStr.length() == 0 ||
                        fatStr.length() == 0 || fibStr.length() == 0 || sodStr.length() == 0) {
                        Toast.makeText(getActivity(), "Please fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<String> halls = new ArrayList<>();

                    if (owensHG.isChecked()) {
                        halls.add("Owens");
                    }
                    if (b37.isChecked()) {
                        halls.add("b37");
                    }
                    if (d2dx.isChecked()) {
                        halls.add("D2");
                    }
                    if (deets.isChecked()) {
                        halls.add("Deets");
                    }
                    if (westEnd.isChecked()) {
                        halls.add("West End");
                    }
                    if (turner.isChecked()) {
                        halls.add("Turner");
                    }
                    MMResultSet rs = foodDB.search(cals.getText().toString(), carbs.getText().toString(),
                            protein.getText().toString(), fat.getText().toString(),
                            fiber.getText().toString(), sodium.getText().toString(),
                            halls);

                    if (rs.getNumResults() > 0) {
                        retainFrag.setResultSet(rs);
                        ScheduleMealCandidateFragment smcf = new ScheduleMealCandidateFragment();
                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainScrollView, smcf, "SCHEDULEMEALCANDIDATEFRAGMENT");
                        fragmentTransaction.addToBackStack("SCHEDULEMEALCANDIDATEFRAGMENT").commit();
                    } else {
                        Toast.makeText(getActivity(), "Sorry, no food items match your parameters. \n " +
                                "Please adjust your parameters and try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnAddMealMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapFrag mf = new MapFrag();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainScrollView, mf, "MAPFRAGMENT");
                fragmentTransaction.addToBackStack("MAPFRAGMENT").commit();
            }
        });
    }

    private void populateUI() {
        Setting set = userInfoDB.getUserSetting();
        NutritionCalculator calc = new NutritionCalculator(set);
        cals.setText(calc.getCals());
        carbs.setText(calc.getCarbs());
        protein.setText(calc.getProtein());
        fat.setText(calc.getFat());
        fiber.setText(calc.getFiber());
        sodium.setText(calc.getSodium());
    }

}
