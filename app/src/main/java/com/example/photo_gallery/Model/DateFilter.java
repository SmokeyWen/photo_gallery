package com.example.photo_gallery.Model;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.Date;
import java.util.stream.Stream;

public class DateFilter extends FilterDecorator {
    private Date filterStartTimeStamp;
    private Date filterEndTimeStamp;

    public DateFilter(IFilter filter, Date filterStartTimeStamp, Date filterEndTimeStamp) {
        super(filter);
        this.filterStartTimeStamp = filterStartTimeStamp;
        this.filterEndTimeStamp = filterEndTimeStamp;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Stream<File> filterPhotos(Stream<File> photos) {
        Stream<File> filterdPhotos = this.getFilter().filterPhotos(photos);
//        ArrayList<String> photo_res = new ArrayList<String>();
        Stream<File> findFileStream = filterdPhotos.filter(f -> filterStartTimeStamp == null && filterEndTimeStamp == null || f.lastModified() >= filterStartTimeStamp.getTime() && f.lastModified() <= filterEndTimeStamp.getTime());
//        findFileStream.forEach(f -> photo_res.add(f.getPath()));
        return findFileStream;
    }
}