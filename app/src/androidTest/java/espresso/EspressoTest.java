package espresso;

import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.Espresso.onView;
import com.example.photo_gallery.R;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import android.view.View;

import android.widget.TextView;

import androidx.test.espresso.Espresso;


import com.example.photo_gallery.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EspressoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void selectSearchBtn() {
        onView(withText("search")).perform(click());
        onView(withId(R.id.SearchActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void selectADateRange() {
        String fromTimeStamp = "2022-04-16 15:40:49";
        String toTimeStamp = "2022-04-16 15:40:51";
        String correctTimeStamp = "2022-04-16 15:40:50";

//      Go to search screen
        onView(withText("search")).perform(click());
        onView(withId(R.id.SearchActivity)).check(matches(isDisplayed()));

//      Enter the desired timestamp and verify
        onView(withId(R.id.etFromDateTime)).perform(click()).perform(replaceText(fromTimeStamp));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etFromDateTime)).check(matches(withText(fromTimeStamp)));
        onView(withId(R.id.etToDateTime)).perform(click()).perform(replaceText(toTimeStamp));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etToDateTime)).check(matches(withText(toTimeStamp)));

//      Go back to the main screen and verify
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));

//      Check the timstamp on main screen has the correct timestamp
        onView(withId(R.id.tvTimestamp)).check(matches(withText(correctTimeStamp)));
    }
}
