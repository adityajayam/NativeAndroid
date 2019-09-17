package demo.photogallery;

import android.app.Application;

public class FlickrApplication extends Application {
    private static int page_count = 1;

    public static int getPage_count() {
        return page_count;
    }

    public static void setPage_count(int page_count) {
        FlickrApplication.page_count = page_count;
    }
}
