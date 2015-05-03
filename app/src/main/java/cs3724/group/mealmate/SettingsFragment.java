package cs3724.group.mealmate;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";

    // retained fragment
    RetainedFragment retainFrag;

    // user info DB
    DatabaseHandler userInfoDB;

    // UI Elements
    EditText age;
    Spinner sex;
    EditText height;
    EditText weight;
    EditText reminder;
    CheckBox usePebble;
    Button save;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // get the retained fragment
        retainFrag = (RetainedFragment) getFragmentManager()
                .findFragmentByTag(FRAG_RETAIN_TAG);

        // get UI elements
        age = (EditText) view.findViewById(R.id.settingsEditAge);
        sex = (Spinner) view.findViewById(R.id.spinnerGender);
        height = (EditText) view.findViewById(R.id.settingsEditHeight);
        weight = (EditText) view.findViewById(R.id.settingsEditWeight);
        reminder = (EditText) view.findViewById(R.id.settingsEditReminder);
        usePebble = (CheckBox) view.findViewById(R.id.checkBoxSettings);
        save = (Button) view.findViewById(R.id.btnSettingsSaveChanges);

        // set on click listener
        save.setOnClickListener(this);

        userInfoDB = retainFrag.getUserInfoDB();

        // populate settings if exists
        Setting set = userInfoDB.getUserSetting();
        if (set != null) {
            age.setText(set.getAge());
            int sexIndex;
            if (set.getSex().equals("Male")) {
                sexIndex = 0;
            } else {
                sexIndex = 1;
            }
            sex.setSelection(sexIndex);
            height.setText(set.getHeight());
            weight.setText(set.getWeight());
            reminder.setText(set.getReminderTime());
            //System.out.println(set.getReminderTime());
            if (set.isPebbleConnected()) {
                usePebble.setChecked(true);
            }
        }

        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSettingsSaveChanges) {
            // get values from UI elements
            String ageStr = age.getText().toString();
            String sexStr = sex.getSelectedItem().toString();
            String heightStr = height.getText().toString();
            String weightStr = weight.getText().toString();
            String reminderStr = reminder.getText().toString();

            if (ageStr.length() != 0 && heightStr.length() != 0 &&
                    weightStr.length() != 0 && reminderStr.length() != 0) {
                // create setting
                Setting newSetting =
                        new Setting(ageStr, sexStr, heightStr, weightStr, reminderStr, usePebble.isChecked());

                // insert setting into DB
                userInfoDB.addSetting(newSetting);
                //System.out.println(ageStr + sexStr + heightStr + weightStr + reminderStr + ".");
                //System.out.println(newSetting.getReminderTime());
                Toast.makeText(getActivity(), "Setting saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity().getBaseContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
