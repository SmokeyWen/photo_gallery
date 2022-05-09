package com.example.photo_gallery.Model;

import com.example.photo_gallery.Model.IFilter;

import java.io.File;
import java.util.stream.Stream;

public class BasicFilter implements IFilter {
    @Override
    public Stream<File> filterPhotos(Stream<File> photos) {
        return photos;
    }
}
