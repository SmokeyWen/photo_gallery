package com.example.photo_gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

public abstract class FilterDecorator implements IFilter {
    protected IFilter filter;

    public final String filterCaption;
    public final Date filterStartTimeStamp;
    public final Date filterEndTimeStamp;
    public final String latLng;

    public FilterDecorator(IFilter filter, String filterCaption, Date filterStartTimeStamp, Date filterEndTimeStamp, String latLng) {
        this.filter = filter;
        this.filterCaption = filterCaption;
        this.filterStartTimeStamp = filterStartTimeStamp;
        this.filterEndTimeStamp = filterEndTimeStamp;
        this.latLng = latLng;
    }

    @Override
    public ArrayList<String> filterPhotos(Stream<File> photos) {
        this.filter.filterPhotos(photos);
        return null;
    }
}
