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

public class ScheduleAddMealViewFragment extends Fragment {
    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";

    // retained fragment
    RetainedFragment retainFrag;

    // DBs
    DatabaseHandler userInfoDB;
    SQLiteHelper foodDB;

    // UI Elements
    EditText cals;
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

    private void setGlobals(View view){
       btnAddMealGo = (Button) view.findViewById(R.id.btnScheduleAddMealGo);
    }

    private void setListeners(){
        btnAddMealGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleMealCandidateFragment smcf = new ScheduleMealCandidateFragment();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainScrollView, smcf, "SCHEDULEMEALCANDIDATEFRAGMENT");
                fragmentTransaction.addToBackStack("SCHEDULEMEALCANDIDATEFRAGMENT").commit();
            }
        });
    }

    private void populateUI() {
        Setting set = userInfoDB.getUserSetting();
        NutritionCalculator calc = new NutritionCalculator(set);
        cals.setText(calc.getCals());
        protein.setText(calc.getProtein());
        fat.setText(calc.getFat());
        fiber.setText(calc.getFiber());
        sodium.setText(calc.getSodium());
    }

}
