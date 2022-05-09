package com.example.photo_gallery.Model;

import com.example.photo_gallery.Model.IFilter;

import java.io.File;
import java.util.stream.Stream;

public abstract class FilterDecorator implements IFilter {
    private IFilter filter;

//    public final String filterCaption;
//    public final Date filterStartTimeStamp;
//    public final Date filterEndTimeStamp;
//    public final String latLng;

    public FilterDecorator(IFilter filter) {
        this.filter = filter;
//        this.filterCaption = filterCaption;
//        this.filterStartTimeStamp = filterStartTimeStamp;
//        this.filterEndTimeStamp = filterEndTimeStamp;
//        this.latLng = latLng;
    }

    public IFilter getFilter() {
        return this.filter;
    };

    @Override
    public Stream<File> filterPhotos(Stream<File> photos) {
        this.filter.filterPhotos(photos);
        return null;
    }
}
