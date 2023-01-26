package com.example.photo_gallery;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;

import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class SearchPhotosAcceptanceTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);
    public static void setFromDate(int year, int monthOfYear, int dayOfMonth) {
        onView(withId(R.id.etFromDateTime)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }
    public static void setToDate(int year, int monthOfYear, int dayOfMonth) {
        onView(withId(R.id.etToDateTime)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }
    @Test /* Find Photos Test */
    public void timeBasedSearch() throws Exception {
        Date startTimeStamp = null, endTimeStamp = null;
        try {
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
            // Set the following to the timestamp of one of the photos in the pictures folder
            startTimeStamp = format.parse("20220929_104502");
            calendar.setTime(startTimeStamp);
            startTimeStamp = calendar.getTime();
            calendar.add(Calendar.MINUTE, 1);
            endTimeStamp = format.parse("20221001_090000");
            calendar.setTime(endTimeStamp);
            endTimeStamp = calendar.getTime();
            calendar.add(Calendar.MINUTE, 1);

        } catch (Exception ex) { }
        // Find the Click and Search Button
        onView(withText("search")).perform(click());
        // Find From and To fields in the Search layout and fill these with the above test data
        String from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(startTimeStamp);
        String to = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(endTimeStamp);
        String[] fromSplit = from.split(" ");
        String[] fromDateSplit = fromSplit[0].split("-");
        Integer fromYear = Integer.parseInt(fromDateSplit[0]);
        Integer fromMonth = Integer.parseInt(fromDateSplit[1]);
        Integer fromDay = Integer.parseInt(fromDateSplit[2]);
        String[] toSplit = to.split(" ");
        String[] toDateSplit = toSplit[0].split("-");
        Integer toYear = Integer.parseInt(toDateSplit[0]);
        Integer toMonth = Integer.parseInt(toDateSplit[1]);
        Integer toDay = Integer.parseInt(toDateSplit[2]);
        onView(withId(R.id.etFromDateTime)).perform(replaceText(from), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(replaceText(to), closeSoftKeyboard());
        //Leave the keywords field blank
        onView(withId(R.id.etKeywords)).perform(replaceText(""), closeSoftKeyboard());
        //Find and Click the Go button on the Search View
        onView(withId(R.id.go)).perform(click());
        //Verify that the timestamp of the found Image matches the Expected Value
        onView(withId(R.id.tvTimestamp)).check(matches(withText(containsString("20220929_104502"))));
        //Find and Click the Next Button
        onView(withText("next")).perform(click());
        //Verify that the timestamp of the found image matches the expected value
        onView(withId(R.id.tvTimestamp)).check(matches(withText(containsString("20221004_162021"))));
        // Find the Click and Search Button
        onView(withText("search")).perform(click());
        //Leave the keywords field blank
        onView(withId(R.id.etKeywords)).perform(replaceText("NewCaption"), closeSoftKeyboard());
        onView(withId(R.id.etFromDateTime)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(replaceText(""), closeSoftKeyboard());
        //Find and Click the Go button on the Search View
        onView(withId(R.id.go)).perform(click());
        //Verify that the timestamp of the found image matches the expected value
        onView(withId(R.id.etCaption)).check(matches(withText(containsString("NewCaption"))));
        onView(withText("search")).perform(click());
        SearchPhotosAcceptanceTest.setFromDate(fromYear, fromMonth, fromDay);
        SearchPhotosAcceptanceTest.setToDate(toYear, toMonth, toDay);
        onView(withId(R.id.go)).perform(click());
        //Verify that the timestamp of the found Image matches the Expected Value
        onView(withId(R.id.tvTimestamp)).check(matches(withText(containsString("20220929_104502"))));
        //Find and Click the Next Button
        onView(withText("next")).perform(click());
        //Verify that the timestamp of the found image matches the expected value
        onView(withId(R.id.tvTimestamp)).check(matches(withText(containsString("20221004_162021"))));

    }
}
