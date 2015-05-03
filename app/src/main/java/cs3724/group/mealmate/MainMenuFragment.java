package cs3724.group.mealmate;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class MainMenuFragment extends Fragment {
    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";
    private final static String HISTORY_MODE = "history";
    private final static String SCHEDULE_MODE = "schedule";
    private final static String HUNGRY_MODE = "hungry";

    ImageView imgHungry, imgHistory, imgSchedule, imgSetting;
    RetainedFragment retainFrag;
    DatabaseHandler userInfoDB;

    public MainMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        setGlobals(view);
        setListeners(view);

        retainFrag = (RetainedFragment) getFragmentManager()
                .findFragmentByTag(FRAG_RETAIN_TAG);
        checkSetting();

        return view;
    }

    private void setGlobals(View view){
        imgHungry = (ImageView) view.findViewById(R.id.imgViewImHungry);
        imgHistory = (ImageView) view.findViewById(R.id.imgViewHistory);
        imgSchedule = (ImageView) view.findViewById(R.id.imgViewSchedule);
        imgSetting = (ImageView) view.findViewById(R.id.imgViewSettings);
    }

    private void setListeners(View view){
        imgHungry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClick(v);
            }
        });
        imgHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               OnClick(v);
            }
        });
        imgSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClick(v);
            }
        });
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClick(v);
            }
        });
    }

    public void OnClick(View v){
        if(v.getId() == imgHungry.getId()){
            //Toast.makeText(getActivity(), "Clicked I'm Hungry", Toast.LENGTH_SHORT).show();
            retainFrag.setMode(HUNGRY_MODE);
            ScheduleAddMealViewFragment sadmvf = new ScheduleAddMealViewFragment();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainScrollView, sadmvf, "SCHEDULEADDMEALVIEWWFRAGMENT");
            fragmentTransaction.addToBackStack("SCHEDULEADDMEALVIEWWFRAGMENT").commit();
        }
        if(v.getId() == imgHistory.getId()){
            //Toast.makeText(getActivity(), "Clicked History", Toast.LENGTH_SHORT).show();
            retainFrag.setMode(HISTORY_MODE);
            HistoryViewFragment hsvf = new HistoryViewFragment();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainScrollView, hsvf, "HISTORYVIEWFRAGMENT");
            fragmentTransaction.addToBackStack("HISTORYVIEWFRAGMENT").commit();
        }
        if(v.getId() == imgSchedule.getId()){
            //Toast.makeText(getActivity(), "Clicked Schedule", Toast.LENGTH_SHORT).show();
            retainFrag.setMode(SCHEDULE_MODE);
            ScheduleViewFragment svf = new ScheduleViewFragment();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainScrollView, svf, "SCHEDULEVIEWFRAGMENT");
            fragmentTransaction.addToBackStack("SCHEDULEVIEWFRAGMENT").commit();
        }
        if(v.getId() == imgSetting.getId()){
            //Toast.makeText(getActivity(), "Clicked Setting", Toast.LENGTH_SHORT).show();
            SettingsFragment sf = new SettingsFragment();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainScrollView, sf, "SETTINGSFRAGMENT");
            fragmentTransaction.addToBackStack("SETTINGSFRAGMENT").commit();
        }
    }

    public void checkSetting() {
        if(retainFrag == null) {
            imgHungry.setEnabled(false);
            imgHistory.setEnabled(false);
            imgSchedule.setEnabled(false);
            Toast.makeText(getActivity(), "Please create your setting before continuing",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (retainFrag.getUserInfoDB().getUserSetting() == null) {
                imgHungry.setEnabled(false);
                imgHistory.setEnabled(false);
                imgSchedule.setEnabled(false);
                Toast.makeText(getActivity(), "Please create your setting before continuing",
                        Toast.LENGTH_SHORT).show();
            } else {
                imgHungry.setEnabled(true);
                imgHistory.setEnabled(true);
                imgSchedule.setEnabled(true);
            }
        }
    }

    public ImageView getImgHungry() {
        return imgHungry;
    }

    public ImageView getImgSchedule() {
        return imgSchedule;
    }

    public ImageView getImgHistory() {
        return imgHistory;
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
