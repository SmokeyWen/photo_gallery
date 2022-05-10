package com.example.photo_gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

public abstract class FilterDecorator implements IFilter {
    private IFilter filter;

    public FilterDecorator(IFilter filter) {
        this.filter = filter;
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
