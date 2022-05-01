package com.example.photo_gallery.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import com.example.photo_gallery.Model.Photo;
import com.example.photo_gallery.Model.PhotoRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class GalleryPresenter {
    private Activity context;
    private PhotoRepository repository;
    private ArrayList<Photo> photos = null;

    public GalleryPresenter(Activity context) {
        this.context = context;
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.photo_gallery.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    public void search() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photo_gallery/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();

        if (fList != null) {
            Stream<File> fileStream = Arrays.stream(fList);
            Stream<File> findFileStream = fileStream.filter(f -> ((filterStartTimestamp == null && filterEndTimestamp == null) || (f.lastModified() >= filterStartTimestamp.getTime() && f.lastModified() <= filterEndTimestamp.getTime())) && (filterCaption == "" || filterCaption == null || f.getPath().contains(filterCaption)));
            findFileStream.forEach(f -> photos.add(f.getPath()));
        }
        return photos;
    }

    public void onReturn(int requestCode, int resultCode, Intent data) { }
    public void handleNavigationInput(String navigationAction, String caption)  { }
    public interface View {
        public void displayPhoto(Bitmap photo, String caption, String timestamp, boolean isFirst, boolean isLast);
    }

}
