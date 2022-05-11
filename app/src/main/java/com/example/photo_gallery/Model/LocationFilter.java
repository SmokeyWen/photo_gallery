package com.example.photo_gallery.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.photo_gallery.Model.FilterDecorator;
import com.example.photo_gallery.Model.IFilter;

import java.io.File;
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
        Stream<File> findFileStream = filterdPhotos.filter(f -> ((latLng == "" || latLng == null || f.getPath().contains(latLng))));
        return findFileStream;
    }
}
