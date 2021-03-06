package com.example.photo_gallery.Model;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

public class PhotoRepository implements IPhotoRepository {

    private Context context;
    public PhotoRepository(Context context) {
        this.context = context;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public ArrayList<Photo> findPhotos(IFilter filter) {
        Log.i("filtering photos on: ", filter.toString());
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photo_gallery/files/Pictures");
        ArrayList<Photo> photos = new ArrayList<>();
        File[] fList = file.listFiles();

        if (fList != null)
            filter.filterPhotos(Arrays.stream(fList)).forEach(f -> photos.add(new Photo(f)));

        Log.i("photos", photos.toString());

        return photos;
    }

    @Override
    public Photo create() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "#caption#" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image;
        try {
            image = File.createTempFile(imageFileName, ".jpg",storageDir);
        } catch (IOException e) {
            image = null;
        }
        String mCurrentPhotoPath = image.getAbsolutePath();
        return new Photo(image);
    }

}
