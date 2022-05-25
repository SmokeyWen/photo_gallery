package espresso;

import static androidx.test.espresso.Espresso.pressBackUnconditionally;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.Espresso.onView;
import com.example.photo_gallery.R;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import androidx.test.espresso.Espresso;


import com.example.photo_gallery.MainActivity;

import org.junit.AfterClass;
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
        String fromTimeStamp = "2022-04-15 14:00:00";
        String toTimeStamp = "2022-04-16 20:00:00";
        String correctTimeStamp = "20220416_200137_4659152328269910865";

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

    @Test
    public void searchWithGivenCaption(){
//      Go to search screen
        onView(withText("search")).perform(click());
        onView(withId(R.id.SearchActivity)).check(matches(isDisplayed()));

        //Searching the Caption
        onView(withId(R.id.etKeywords)).perform(typeText("TEST"));
        Espresso.closeSoftKeyboard();

        //Clicking on search
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));

        //Matches with Caption
        onView(withId(R.id.etCaption)).check(matches(withSubstring("TEST")));
    }

    @Test
    public void selectADateRangeAndCaption() {
        String fromTimeStamp = "2022-04-15 14:00:00";
        String toTimeStamp = "2022-04-16 20:00:00";
        String correctTimeStamp = "20220415_200105_4048891414302013295";

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

//      Searching the Caption
        onView(withId(R.id.etKeywords)).perform(typeText("TEST"));
        Espresso.closeSoftKeyboard();

//      Go back to the main screen and verify
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));

//      Check the timestamp on main screen has the correct timestamp
        onView(withId(R.id.tvTimestamp)).check(matches(withText(correctTimeStamp)));
        onView(withId(R.id.etCaption)).check(matches(withSubstring("TEST")));
    }

    @Test
    public void selectALatAndLng() {
        String lat = "3.333";
        String lng = "4.444";
        String fromTimeStamp = "2022-04-16 15:00:00";
        String toTimeStamp = "2022-04-16 16:00:00";
        String correctCaption = "BCIT_downtown";

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

//      Enter the desired Lat and Lng
        onView(withId(R.id.etLat)).perform(click()).perform(replaceText(lat));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etLat)).check(matches(withText(lat)));
        onView(withId(R.id.etLng)).perform(click()).perform(replaceText(lng));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etLng)).check(matches(withText(lng)));

//      Go back to the main screen and verify
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));

//      Check the caption on main screen has the correct one
        onView(withId(R.id.etCaption)).check(matches(withText(correctCaption)));
    }

    @Test
    public void selectLatLngAndCaption() {
        String lat = "1.111";
        String lng = "2.222";
        String fromTimeStamp = "2022-04-15 15:00:00";
        String toTimeStamp = "2022-04-15 16:00:00";
        String caption = "BCIT_burnaby";

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

//      Enter the desired Lat and Lng
        onView(withId(R.id.etLat)).perform(click()).perform(replaceText(lat));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etLat)).check(matches(withText(lat)));
        onView(withId(R.id.etLng)).perform(click()).perform(replaceText(lng));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etLng)).check(matches(withText(lng)));

//      Searching the Caption
        onView(withId(R.id.etKeywords)).perform(typeText(caption));
        Espresso.closeSoftKeyboard();

//      Go back to the main screen and verify
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));

//      Check the caption on main screen has the correct one
        onView(withId(R.id.etCaption)).check(matches(withText(caption)));
    }

    @Test
    public void selectSnap() {
        onView(withId(R.id.snap)).perform(click()); //click on "Snap" button
        pressBack(); //hit back button. back to app mainActivity
    }

    //test the next button. hit it 3 times
    @Test
    public void hitNext() {
        int threeTimes = 3;
        for(int i = 0; i < threeTimes; ++i) {
            onView(withId(R.id.btnNext)).perform(click());
        }
    }

    //test previous button. hit it 3 times
    @Test
    public void hitPrev() {
        int threeTimes = 3;
        for(int i = 0; i < threeTimes; ++i) {
            onView(withId(R.id.btnPrev)).perform(click());
        }
    }

    @AfterClass
    public void tearDown() {
        pressBackUnconditionally(); //leave the app
        
    }

}
