package cs3724.group.mealmate;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Pat on 5/4/2015.
 */
public class AboutFragment extends Fragment {
    private TextView about;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        about = (TextView) view.findViewById(R.id.textViewAbout);
        setAbout();
        return view;
    }

    private void setAbout() {
        about.setText("MealMate is your eating companion at Virginia Tech. Schedule meals, create goals, and compare those goals against your history. \n\n"
                + "Settings: Make sure to go to settings and enter your physical information. This information is used to calculate recommended nutritional values per meal for you."
                + "Also set how much earlier before your scheduled meals you want to be reminded of those meals. These reminders will be sent to your phone and also your pebble watch if you want!\n\n"
        + "I'm Hungry: Use this to have food items recommended for you. \n"
        + "If you tap on this image, it will take you to the search screen where your recommended values per meal for calories, carbs, protein, fat, fiber, and sodium are pre-entered in the fields for you."
                +"Feel free to change these to your liking. If you tap on 'Map View' This will take you the a map of all the dining halls at VT along with your current location (blue dot)."
                + "Tap on each marker to reveal the name of the dining hall. \n"
        + "Once you're ready to search, hit 'Go!'. This will take you to the results screen. I'm hungry displays 5 random results that meet your search parameters."
               + "Use this if you want to try something new or don't want to waste time deciding what to eat!\n"
        + "You can select multiple foods at a time and they will be added to your schedule. \n\n"
        + "Schedule: You'll find all your scheduled meals here!\n"
        + "Here select a start date, and all your scheduled items for the following day, week, or month will be displayed. You can remove scheduled items, or you can move them to history by pressing 'To History'' if you ate this food and want to move it to your history!\n\n"
        + "History and Goals: Find all your history here and compare it your goals!\n"
        + "Here select a start date, and all the foods you ate for the following day, week, or month will be displayed.\n"
        + "Tap on 'Goals' to see your goals and how you are doing against them. All your goals are displayed here, along with whether you are meeting that goal or not."
                + "Your totals for the past day, week, or month are shown at the bottom.\n"
        + "Tap 'Add Goal' to create a new goal. Just select your nutritional target, whether you want more or less of that target, the goal number for the target, and the duration of the goal."
                + "Tap 'Add' and your goal is added to your list of goals!\n\n"
        + "Dining Hall ID Meanings:\n"
                + "West End - West End Market\n"
                        + "B37 - Burger 37\n"
                        + "D2 L+D - D2 Lunch and Dinner Offerings\n"
                        + "D2 Break - D2 Breakfast Offerings\n"
                        + "Deets - Deets Place\n"
                        + "Owens/Hokie Grill - Food at Owens or Hokie Grill\n"
                        + "Turner L&D - Turner Place Lunch & Dinner Offerings");
    }
}
