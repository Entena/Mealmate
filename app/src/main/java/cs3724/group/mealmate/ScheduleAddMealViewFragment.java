package cs3724.group.mealmate;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ScheduleAddMealViewFragment extends Fragment {
    private Button btnAddMealGo;

    public ScheduleAddMealViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_add_meal_view, container, false);
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

}
