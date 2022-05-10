package com.example.photo_gallery;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.ArrayList;
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
        Stream<File> findFileStream = filterdPhotos.filter(f -> ((filterStartTimeStamp == null && filterEndTimeStamp == null) || (f.lastModified() >= filterEndTimeStamp.getTime() && f.lastModified() <= filterEndTimeStamp.getTime())));
        return findFileStream;
    }
}