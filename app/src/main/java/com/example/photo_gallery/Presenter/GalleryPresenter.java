package com.example.photo_gallery.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import com.example.photo_gallery.Model.Photo;
import com.example.photo_gallery.Model.PhotoRepository;
import com.example.photo_gallery.SearchActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class GalleryPresenter {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_FILTER = 2;
    private Activity context;
    private PhotoRepository repository;
    private ArrayList<Photo> photos = null;
    private int index = 0;

    public GalleryPresenter(Activity context) {
        this.context = context;
        repository = new PhotoRepository(this.context);
    }

    public void takePhoto(PhotoRepository ph) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            Photo photo = repository.create();
            // Continue only if the File was successfully created
            if (photo.getPhotoFile() != null) {
                Uri photoURI = FileProvider.getUriForFile(context, "com.example.photo_gallery.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                context.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void search() {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivityForResult(intent, REQUEST_IMAGE_FILTER);
//        File file = new File(Environment.getExternalStorageDirectory()
//                .getAbsolutePath(), "/Android/data/com.example.photo_gallery/files/Pictures");
//        ArrayList<String> photos = new ArrayList<String>();
//        File[] fList = file.listFiles();
//
//        if (fList != null) {
//            Stream<File> fileStream = Arrays.stream(fList);
//            Stream<File> findFileStream = fileStream.filter(f -> ((filterStartTimestamp == null && filterEndTimestamp == null) || (f.lastModified() >= filterStartTimestamp.getTime() && f.lastModified() <= filterEndTimestamp.getTime())) && (filterCaption == "" || filterCaption == null || f.getPath().contains(filterCaption)));
//            findFileStream.forEach(f -> photos.add(f.getPath()));
//        }
//        return photos;
    }

    public void scrollPhotos() {
        try {
//            updatePhoto(photos.get(index), ((EditText) findViewById(R.id.etCaption)).getText().toString());
            photos.get(index).setCaption(((EditText) context.findViewById(R.id.etCaption)).getText().toString());
            if (index > (photos.size() - 1)) // with an active caption filter, changing the last image's caption
                index = photos.size() - 1;   // causes errors. this will rectify it.
            if (photos.size() == 0) { // if we remove the only image that existed, we ought to
                ((GalleryPresenter.View) context).displayPhoto(null, "", ""); // display nothing.
                return;
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        switch (v.getId()) {
            case R.id.btnPrev:
                if (index > 0) {
                    index--;
                }
                break;
            case R.id.btnNext:
                if (index < (photos.size() - 1)) {
                    index++;
                }
                break;
            default:
                break;
        }
    }

    public void onReturn(int requestCode, int resultCode, Intent data) { }
    public void handleNavigationInput(String navigationAction, String caption)  { }

    public interface View {
        public void displayPhoto(Bitmap photo, String caption, String timestamp);
    }

}
