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
        if(retainFrag == null) {
            imgHungry.setEnabled(false);
            imgHistory.setEnabled(false);
            imgSchedule.setEnabled(false);
        } else {
            if (retainFrag.getUserInfoDB().getUserSetting() == null) {
                imgHungry.setEnabled(false);
                imgHistory.setEnabled(false);
                imgSchedule.setEnabled(false);
            } else {
                imgHungry.setEnabled(true);
                imgHistory.setEnabled(true);
                imgSchedule.setEnabled(true);
            }
        }

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
            HungryViewFragment hvf = new HungryViewFragment();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainScrollView, hvf, "HUNGRYVIEWFRAGMENT");
            fragmentTransaction.addToBackStack("HUNGRYVIEWFRAGMENT").commit();
        }
        if(v.getId() == imgHistory.getId()){
            //Toast.makeText(getActivity(), "Clicked History", Toast.LENGTH_SHORT).show();
            HistoryViewFragment hsvf = new HistoryViewFragment();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainScrollView, hsvf, "HISTORYVIEWFRAGMENT");
            fragmentTransaction.addToBackStack("HISTORYVIEWFRAGMENT").commit();
        }
        if(v.getId() == imgSchedule.getId()){
            //Toast.makeText(getActivity(), "Clicked Schedule", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
