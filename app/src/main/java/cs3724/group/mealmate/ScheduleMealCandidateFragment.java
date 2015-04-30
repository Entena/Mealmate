package cs3724.group.mealmate;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleMealCandidateFragment extends Fragment {

    public final static String FRAG_RETAIN_TAG = "FRAG_RETAIN";
    private final static int MAX_ENTRIES = 100;

    // retained fragment
    RetainedFragment retainFrag;

    // DBs
    DatabaseHandler userInfoDB;
    SQLiteHelper foodDB;

    // UI Elements
    TableLayout resultsTbl;


    public ScheduleMealCandidateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_meal_candidate, container, false);

        retainFrag = (RetainedFragment) getFragmentManager()
                .findFragmentByTag(FRAG_RETAIN_TAG);

        // get UI elements
        resultsTbl = (TableLayout) view.findViewById(R.id.resultsTable);

        // get DBs
        userInfoDB = retainFrag.getUserInfoDB();
        foodDB = retainFrag.getFoodDB();

        // get results
        //MMResultSet rs = retainFrag.getLastResultSet();
        //MMResultSet rs = foodDB.execQuery("SELECT * FROM foods WHERE CALS < 854 AND PROT > 26 AND FAT < 28 AND FIBER > 12 AND SOD < 657 AND DINING_HALL_ID LIKE '%Owens%'");
        MMResultSet rs = foodDB.execQuery("SELECT * FROM foods WHERE CALS < 854 AND PROT > 26");

        int count = 0;

        while(rs.hasNext() && count < MAX_ENTRIES) {
            TableRow tr = new TableRow(getActivity());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            CheckBox cb = new CheckBox(getActivity());
            cb.setText("Add");
            /*TextView foodName = new TextView(getActivity());
            foodName.setText(rs.getFoodName());*/
            TextView cals = new TextView(getActivity());
            cals.setText(rs.getCalories());
            TextView protein = new TextView(getActivity());
            protein.setText(rs.getProtein());
            TextView fat = new TextView(getActivity());
            fat.setText(rs.getFat());
            TextView fiber = new TextView(getActivity());
            fiber.setText(rs.getFiber());
            TextView sodium = new TextView(getActivity());
            sodium.setText(rs.getSodium());
            //TextView location = new TextView(getActivity());
            //location.setText(rs.getDiningHall());

            tr.addView(cb);
            //tr.addView(location);
            //tr.addView(foodName);
            tr.addView(cals);
            tr.addView(protein);
            tr.addView(fat);
            tr.addView(fiber);
            tr.addView(sodium);

            resultsTbl.addView(tr,
                    new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
            rs.next();
            count++;
        }
        return view;
    }


}
