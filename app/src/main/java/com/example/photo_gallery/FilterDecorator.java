package com.example.photo_gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
