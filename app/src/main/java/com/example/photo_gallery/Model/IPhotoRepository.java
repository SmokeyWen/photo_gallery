package com.example.photo_gallery.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Date;

public interface IPhotoRepository {
    public default ArrayList<Photo> findPhotos(Date StartTimeStamp, Date endTimeStamp, String keywords) {
        return null;
    }

    public default Photo create(){
        return null;
    }
}
