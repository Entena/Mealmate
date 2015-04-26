package cs3724.group.mealmate;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleViewFragment extends Fragment {
    private Button btnAddMeal;

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
        return view;
    }

    private void setGlobals(View view){
        btnAddMeal = (Button) view.findViewById(R.id.btnAddAMeal);
    }

    private void setListeners(){
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
    }

}
