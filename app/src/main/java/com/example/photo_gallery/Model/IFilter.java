package com.example.photo_gallery.Model;

import java.io.File;
import java.util.stream.Stream;

public interface IFilter {
    Stream<File> filterPhotos(Stream<File> photos);
}
