package com.example.photo_gallery;
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




    public static class FilterBuilder {
        private String filterCaption;
        private final Date filterStartTimeStamp;
        private final Date filterEndTimeStamp;

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
