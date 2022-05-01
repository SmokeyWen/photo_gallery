package com.example.photo_gallery.Model;
import java.io.File;
import java.util.Date;

import android.graphics.Bitmap;

public class Photo {
    private File photoFile;
    private Date timeStamp;
    private String caption;
    private Bitmap bitmap;
    private String path;

    public Photo(File photoFile){}

    public File getPhotoFile() {
        return photoFile;
    }

    public Date getTimestamp() {
        return timeStamp;
    }

    public String getCaption() {
        return caption;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public void updateCaption (String caption) {
        String[] attr = path.split("#");
        if (attr.length >= 3) {
            File to = new File(attr[0] + "#" + caption + "#" + attr[2]);
            File from = new File(path);
            from.renameTo(to);
        }
    }
}
