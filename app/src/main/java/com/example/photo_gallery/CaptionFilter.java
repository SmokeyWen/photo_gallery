package com.example.photo_gallery;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

public class CaptionFilter extends FilterDecorator{
    public CaptionFilter(IFilter filter, String filterCaption, Date filterStartTimeStamp, Date filterEndTimeStamp, String latLng) {
        super(filter, filterCaption, filterStartTimeStamp, filterEndTimeStamp, latLng);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public ArrayList<String> filterPhotos(Stream<File> photos) {
        super.filterPhotos(photos);
        ArrayList<String> photo_res = new ArrayList<String>();
        Stream<File> findFileStream = photos.filter(f -> ((filterCaption == "" || filterCaption == null || f.getPath().contains(filterCaption))));
        findFileStream.forEach(f -> photo_res.add(f.getPath()));
        return photo_res;
    }
}
