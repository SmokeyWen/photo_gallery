package com.example.photo_gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;

public class BasicFilter implements IFilter{
    @Override
    public ArrayList<String> filterPhotos(Stream<File> photos) {
        return null;
    }
}
