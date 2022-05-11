package com.example.photo_gallery.Model;
import java.util.Date;

public class Filter {
    private final String filterCaption;
    private final Date filterStartTimeStamp;
    private final Date filterEndTimeStamp;


    private Filter(FilterBuilder filter) {
        this.filterStartTimeStamp = filter.filterStartTimeStamp;
        this.filterEndTimeStamp = filter.filterEndTimeStamp;
        this.filterCaption = filter.filterCaption;
    }

    public Date getFilterStartTimeStamp() {
        return filterStartTimeStamp;
    }

    public Date getFilterEndTimeStamp() {
        return  filterEndTimeStamp;
    }

    public String getFilterCaption() {
        return filterCaption;
    }

    public String toString() {
        String startTimeStamp = this.filterStartTimeStamp != null ? this.filterStartTimeStamp.toString() : "";
        String endTimeStamp = this.filterEndTimeStamp != null ? this.filterEndTimeStamp.toString() : "";
        return startTimeStamp + " - " + endTimeStamp + " : caption - " + this.getFilterCaption();
    }


    public static class FilterBuilder {
        private String filterCaption;
        private final Date filterStartTimeStamp;
        private final Date filterEndTimeStamp;
        public static Filter EMPTY_FILTER =
                new FilterBuilder(new Date(Long.MIN_VALUE), new Date()).build();

        public FilterBuilder(Date filterStartTimeStamp, Date filterEndTimeStamp) {
            this.filterStartTimeStamp = filterStartTimeStamp;
            this.filterEndTimeStamp = filterEndTimeStamp;
        }

        public FilterBuilder withCaption(String filterCaption) {
            this.filterCaption = filterCaption;
            return this;
        }

        public Filter build() {
            Filter filter = new Filter(this);
            return filter;
        }
    }
}
