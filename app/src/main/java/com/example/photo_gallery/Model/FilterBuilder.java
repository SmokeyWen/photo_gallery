package com.example.photo_gallery.Model;

import java.util.Date;

public class FilterBuilder {
    private IFilter filter;

    public FilterBuilder() {
        filter = new BasicFilter();
    }

    public void withDates(Date startTimestamp, Date endTimestamp) {
        filter = new DateFilter(filter, startTimestamp, endTimestamp);
    }

    public void withCaption(String caption) {
        filter = new CaptionFilter(filter, caption);
    }

    public void withLocation(String latlng) {
        filter  = new LocationFilter(filter, latlng);
    }

    public IFilter build() {
        IFilter filterToReturn = filter;
        filter = new BasicFilter();
        return filterToReturn;
    }
}
