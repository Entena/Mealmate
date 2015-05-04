package cs3724.group.mealmate;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AddGoalViewFragment extends Fragment {
    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";

    private RetainedFragment retainFrag;
    // DBs
    private DatabaseHandler userInfoDB;

    //UI elements
    private Button btnAddGoal;
    private Spinner item;
    private Spinner pref;
    private EditText metric;
    private Spinner duration;

    public AddGoalViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_goal_view, container, false);
        setUpGlobals(view);
        setUpListeners();
        return view;
    }

    private void setUpGlobals(View view){
        btnAddGoal = (Button) view.findViewById(R.id.btnAddGoalAdd);
        item = (Spinner) view.findViewById(R.id.spinnerAddGoalItem);
        pref = (Spinner) view.findViewById(R.id.spinnerAddGoalOperators);
        duration = (Spinner) view.findViewById(R.id.spinnerAddGoalDuration);
        metric = (EditText) view.findViewById(R.id.editTextAddGoalMetric);

        retainFrag = (RetainedFragment) getFragmentManager().findFragmentByTag(FRAG_RETAIN_TAG);
        userInfoDB = retainFrag.getUserInfoDB();
    }

    private void setUpListeners() {
        btnAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemStr = item.getSelectedItem().toString();
                String prefStr = pref.getSelectedItem().toString();
                String durStr = duration.getSelectedItem().toString();
                String metricStr = metric.getText().toString();
                if (itemStr.length() == 0 || prefStr.length() == 0 || durStr.length() == 0 ||
                        metricStr.length() == 0) {
                    Toast.makeText(getActivity(), "Please fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Goal g = new Goal(itemStr, (prefStr + " " + metricStr), durStr);
                    userInfoDB.addGoal(g);
                    Toast.makeText(getActivity(), "Goal added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
