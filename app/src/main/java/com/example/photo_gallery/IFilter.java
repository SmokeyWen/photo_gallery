package com.example.photo_gallery;

import java.io.File;
import java.util.stream.Stream;

public interface IFilter {
    Stream<File> filterPhotos(Stream<File> photos);
}
