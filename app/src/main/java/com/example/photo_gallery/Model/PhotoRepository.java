package com.example.photo_gallery.Model;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.RequiresApi;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

public class PhotoRepository implements IPhotoRepository {

    private Context context;
    public PhotoRepository(Context context) {  }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public ArrayList<Photo> findPhotos(Date startTimestamp, Date endTimestamp, String keywords) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photo_gallery/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();

        if (fList != null) {
            Stream<File> fileStream = Arrays.stream(fList);
            Stream<File> findFileStream = fileStream.filter(f -> ((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime() && f.lastModified() <= endTimestamp.getTime())) && (keywords == "" || keywords == null || f.getPath().contains(keywords)));
            findFileStream.forEach(f -> photos.add(f.getPath()));
        }
        return photos;
    }

    @Override
    public Photo create() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "#caption#" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
