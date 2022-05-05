package com.example.photo_gallery.Model;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Photo {
    private File photoFile;
    private Date timeStamp;
    private String caption;
    private Bitmap bitmap;
    private String path;

    public Photo(File photoFile){
        this.photoFile = photoFile;
        this.path = photoFile.getPath();
        String[] attr = this.path.split("#");
        this.caption = attr[1];
        this.bitmap = BitmapFactory.decodeFile(this.path);
        try {
            this.timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").parse(attr[2].split("\\.")[0]);
        } catch (ParseException pe) {
            this.timeStamp = null;
        }
    }

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

    public void setCaption (String caption) {
        String[] attr = path.split("#");
        if (attr.length >= 3) {
            File to = new File(attr[0] + "#" + caption + "#" + attr[2]);
            File from = new File(path);
            from.renameTo(to);
        }
    }
}
