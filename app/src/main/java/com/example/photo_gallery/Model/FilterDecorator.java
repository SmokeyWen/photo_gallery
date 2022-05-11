package com.example.photo_gallery.Model;

import com.example.photo_gallery.Model.IFilter;

import java.io.File;
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
