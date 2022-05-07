package com.example.photo_gallery;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

public class DateFilter extends FilterDecorator {
    public DateFilter(IFilter filter, String filterCaption, Date filterStartTimeStamp, Date filterEndTimeStamp, String latLng) {
        super(filter, filterCaption, filterStartTimeStamp, filterEndTimeStamp, latLng);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public ArrayList<String> filterPhotos(Stream<File> photos) {
        super.filterPhotos(photos);
        ArrayList<String> photo_res = new ArrayList<String>();
        Stream<File> findFileStream = photos.filter(f -> ((filterStartTimeStamp == null && filterEndTimeStamp == null) || (f.lastModified() >= filterEndTimeStamp.getTime() && f.lastModified() <= filterEndTimeStamp.getTime())));
        findFileStream.forEach(f -> photo_res.add(f.getPath()));
        return photo_res;
    }
}