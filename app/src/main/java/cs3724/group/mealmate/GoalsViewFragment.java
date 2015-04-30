package cs3724.group.mealmate;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class GoalsViewFragment extends Fragment {
    private Button btnAddGoal;

    public GoalsViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals_view, container, false);
        setUpGlobals(view);
        setUpListeners();
        return view;
    }

    private void setUpGlobals(View view){
        btnAddGoal = (Button) view.findViewById(R.id.btnAddGoal);
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
    }
}
