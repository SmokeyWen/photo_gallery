package com.example.photo_gallery;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import android.graphics.Bitmap;
import android.os.Environment;

import com.example.photo_gallery.Model.Filter;
import com.example.photo_gallery.Model.FilterBuilder;
import com.example.photo_gallery.Model.Photo;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * local unit tests for Photo Model.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PhotoUnitTests {

    //exceptions
    Exception nullException = new Exception("null");

    //sample test
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    //test that image file we are capturing is grabbing a String type
    @Test
    public void captionCheck() {
        Photo photo = getFirstPhoto();
        boolean actualCaptionType = photo.getCaption() instanceof String;
        assertEquals(true, actualCaptionType);
    }

    //test that image file we are capturing, grabs TimeStamp of Date type
    @Test
    public void timestampCheck() {
        Photo photo = getFirstPhoto();
        boolean timestampType = photo.getTimestamp() instanceof Date;
        assertEquals(true, timestampType);
    }

    //test that Photo object is returning a File
    @Test
    public void fileCheck() {
        Photo photo = getFirstPhoto();
        boolean fileType = photo.getPhotoFile() instanceof File;
        assertEquals(true, fileType);
    }

    //test that photo object has Bitmap
    @Test
    public void bitmapCheck() {
        Photo photo = getFirstPhoto();
        boolean bitmapType = photo.getBitmap() instanceof Bitmap;
        assertEquals(true, bitmapType);
    }

    //test setCaption() method of Photo
    @Test
    public void setCaptionTest() {
        Photo photo = getFirstPhoto();
        photo.setCaption("new caption");
        assertEquals("new caption", photo.getCaption());
    }


    //utility functions ----------

    //gathering photos from external storage. Then put into ArrayList<Photo>
    ArrayList<Photo> gatherPhotos() throws Exception {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photo_gallery/files/Pictures");
        ArrayList<Photo> photos = new ArrayList<>();
        File[] fList = file.listFiles();

        if(fList != null) { //if list not empty
            for( int i = 0; i < fList.length; ++i) {
                Photo p = new Photo(fList[i]);
                photos.add(p);
            }
        } else {
            throw nullException;
        }

        return photos;
    }

    //just to get one photo
    Photo getFirstPhoto() {
        ArrayList<Photo> photos = new ArrayList<Photo>();
        try {
            photos = gatherPhotos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Photo firstPhoto = photos.get(0);
        return firstPhoto;
    }
}