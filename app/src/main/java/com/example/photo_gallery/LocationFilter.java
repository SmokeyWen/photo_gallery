package com.example.photo_gallery;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

public class LocationFilter extends FilterDecorator {
    private String latLng;
    public LocationFilter(IFilter filter, String latLng) {
        super(filter);
        this.latLng = latLng;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Stream<File> filterPhotos(Stream<File> photos) {
        Stream<File> filterdPhotos = this.getFilter().filterPhotos(photos);
//        ArrayList<String> photo_res = new ArrayList<String>();
        Stream<File> findFileStream = filterdPhotos.filter(f -> ((latLng == "" || latLng == null || f.getPath().contains(latLng))));
//        findFileStream.forEach(f -> photo_res.add(f.getPath()));
        return findFileStream;
    }
}
