package cs3724.group.mealmate;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class MainMenuFragment extends Fragment {
    ImageView imgHungry, imgHistory, imgSchedule, imgSetting;

    public MainMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        imgHungry = (ImageView) view.findViewById(R.id.imgViewImHungry);
        imgHistory = (ImageView) view.findViewById(R.id.imgViewHistory);
        imgSchedule = (ImageView) view.findViewById(R.id.imgViewSchedule);
        imgSetting = (ImageView) view.findViewById(R.id.imgViewSettings);
        return view;
    }

    public void OnClick(View v){
        if(v.getId() == imgHungry.getId()){

        }
        if(v.getId() == imgHistory.getId()){

        }
        if(v.getId() == imgSchedule.getId()){

        }
        if(v.getId() == imgSetting.getId()){
            
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
