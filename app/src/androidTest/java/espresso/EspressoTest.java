package espresso;

import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.Espresso.onView;
import com.example.photo_gallery.R;
import static androidx.test.espresso.assertion.ViewAssertions.matches;


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
}
