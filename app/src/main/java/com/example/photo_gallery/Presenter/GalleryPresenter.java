package com.example.photo_gallery.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.example.photo_gallery.Model.BasicFilter;
import com.example.photo_gallery.Model.Filter;
import com.example.photo_gallery.Model.FilterBuilder;
import com.example.photo_gallery.Model.IFilter;
import com.example.photo_gallery.Model.Photo;
import com.example.photo_gallery.Model.PhotoRepository;
import com.example.photo_gallery.R;
import com.example.photo_gallery.SearchActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

public class GalleryPresenter {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_FILTER = 2;
    private Activity context;
    private PhotoRepository repository;
    private ArrayList<Photo> photos = null;
    private IFilter currFilter;
    private FilterBuilder filterBuilder;
    private int index = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public GalleryPresenter(Activity context) {
        this.context = context;
        repository = new PhotoRepository(context);
        filterBuilder = new FilterBuilder();
        index = 0;
        currFilter = new BasicFilter();
        photos = repository.findPhotos(currFilter);
        photos.stream().forEach(x -> Log.i("test", x.getCaption()));
        if (photos.size() > 0) {
            Photo firstPhoto = photos.get(index);
            ((GalleryPresenter.View) this.context).displayPhoto(firstPhoto.getBitmap(),
                    firstPhoto.getCaption(), firstPhoto.getTimestamp());
        }
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            Photo photo = repository.create();
            // Continue only if the File was successfully created
            if (photo.getPhotoFile() != null) {
                currFilter = new BasicFilter(); // reset filter to empty filter
                Uri photoURI = FileProvider.getUriForFile(context, "com.example.photo_gallery.fileprovider", photo.getPhotoFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                context.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public IFilter buildFilter(Date filterStartTimestamp, Date filterEndTimestamp, String filterCaption) {
        Log.i("StarttimeStamp", filterStartTimestamp != null ? filterStartTimestamp.toString() : "");
        Log.i("EndTimeStamp", filterEndTimestamp != null ? filterEndTimestamp.toString() : "");
        Log.i("filterCaption", filterCaption != null ? filterCaption : "NULL");

        if (filterStartTimestamp != null || filterEndTimestamp != null)
            filterBuilder.withDates(filterStartTimestamp, filterEndTimestamp);
        if (filterCaption != null && filterCaption != "")
            filterBuilder.withCaption(filterCaption);
        return filterBuilder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterAndDisplay(Date filterStartTimestamp, Date filterEndTimestamp, String filterCaption) {
        index = 0;
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photo_gallery/files/Pictures");
        File[] fList = file.listFiles();
        currFilter = buildFilter(filterStartTimestamp, filterEndTimestamp, filterCaption);
        photos = repository.findPhotos(currFilter);
        Photo photoToDisplay = null;
        if (photos.size() > 0) {
            photoToDisplay = photos.get(index);
            ((GalleryPresenter.View) context).displayPhoto(photoToDisplay.getBitmap(),
                    photoToDisplay.getCaption(), photoToDisplay.getTimestamp());
        } else {
            ((GalleryPresenter.View) context).displayPhoto(null, "", null);
        }

    }

    public void search() {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivityForResult(intent, REQUEST_IMAGE_FILTER);
    }


    public void onReturn(int requestCode, int resultCode, Intent data) { }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void handleNavigationInput(String navigationAction, String caption)  {
        try {
            photos.get(index).setCaption(caption); // update caption
            photos = repository.findPhotos(currFilter); // refilter list of photos
            if (index > (photos.size() - 1)) // with an active caption filter, changing the last image's caption
                index = photos.size() - 1;   // causes errors. this will rectify it.
            if (photos.size() == 0) { // if we remove the only image that existed, we ought to
                ((GalleryPresenter.View) context).displayPhoto(null, "", new Date()); // display nothing.
                return;
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        } catch (NullPointerException npE) {
            return;
        }

        switch (navigationAction) {
            case "ScrollPrev":
                if (index > 0) {
                    index--;
                }
                break;
            case "ScrollNext":
                Log.i("index", String.valueOf(index));
                Log.i("photos.size()", String.valueOf(photos.size()));
                if (index < (photos.size() - 1)) {
                    index++;
                }
                break;
            default:
                break;
        }
        this.displayPhoto();
    }

    public void displayPhoto() {
        Photo photo = photos.get(index);
        ((GalleryPresenter.View) context).displayPhoto(photo.getBitmap(),
                photo.getCaption(), photo.getTimestamp());
    }

    public interface View {
        public void displayPhoto(Bitmap photo, String caption, Date timestamp);
    }

}
