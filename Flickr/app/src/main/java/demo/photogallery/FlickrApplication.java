package demo.photogallery;

import android.app.Application;

import demo.photogallery.model.GalleryItem;

public class FlickrApplication extends Application {
    private static int page_count = 1;

    private static int APP_THEME_MODE_POSITION = 0;

    private static GalleryItem selectedGalleryItem;

    public static int getPageCount() {
        return page_count;
    }

    public static void setPageCount(int page_count) {
        FlickrApplication.page_count = page_count;
    }

    public static int getAppThemeMode() {
        return APP_THEME_MODE_POSITION;
    }

    public static void setAppThemeMode(int appThemeModePosition) {
        APP_THEME_MODE_POSITION = appThemeModePosition;
    }

    public static GalleryItem getSelectedGalleryItem() {
        return selectedGalleryItem;
    }

    public static void setSelectedGalleryItem(GalleryItem selectedGalleryItem) {
        FlickrApplication.selectedGalleryItem = selectedGalleryItem;
    }
}
