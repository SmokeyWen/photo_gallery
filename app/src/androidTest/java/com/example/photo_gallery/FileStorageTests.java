package com.example.photo_gallery;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.os.Build;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.photo_gallery.db.FileStorage;

@RunWith(AndroidJUnit4.class)
public class FileStorageTests {

    @Before /*Initialization */
    public void grantPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + InstrumentationRegistry.getTargetContext().getPackageName()
                            + " android.permission.READ_EXTERNAL_STORAGE");
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + InstrumentationRegistry.getTargetContext().getPackageName()
                            + " android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    @Test /*Unit Test for findPhotos method */
    public void findPhotosTest() throws Exception {
        // Using the App Context create an instance of the FileStorage
        Context appContext = InstrumentationRegistry.getTargetContext();
        FileStorage fs = new FileStorage(appContext);

        //Test time based search //Initialize a time window around the time a Photo was taken
        Date startTimestamp = null, endTimestamp = null;
        try {
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
            startTimestamp = format.parse("20220929_120929");
            calendar.setTime(startTimestamp);
            calendar.add(Calendar.MINUTE, 1);
            endTimestamp = format.parse("20221001_090000");
            calendar.setTime(endTimestamp);
            endTimestamp = calendar.getTime();
            calendar.add(Calendar.MINUTE, 1);
        } catch (Exception ex) { }

        //Call the method specifying the test time window.
        ArrayList<String> photos = fs.findPhotos(startTimestamp, endTimestamp, "");
        //Verify that multiple photos with the matching timestamp is found
        assertEquals(3, photos.size());
        assertEquals(true, photos.get(0).contains("20220929_120929"));
        assertEquals(true, photos.get(1).contains("20220929_121812"));
        assertEquals(true, photos.get(2).contains("20220929_181045"));
        //Verify that a photo with new caption is found
        ArrayList<String> keywordPhotos = fs.findPhotos(null, null, "NewCaption");
        assertEquals(1, keywordPhotos.size());
        assertEquals(true, keywordPhotos.get(0).contains("NewCaption"));
    }
}
