package com.example.photo_gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;

public interface IFilter {
    ArrayList<String> filterPhotos(Stream<File> photos);
}
