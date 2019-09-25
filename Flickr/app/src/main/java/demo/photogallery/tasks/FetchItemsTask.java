package demo.photogallery.tasks;

import android.os.AsyncTask;

import java.util.List;

import demo.photogallery.FlickrApplication;
import demo.photogallery.FlickrFetcher;
import demo.photogallery.adapters.PhotoAdapter;
import demo.photogallery.model.GalleryItem;
import demo.photogallery.util.DialogUtil;

public class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
    private String mQuery;
    private PhotoAdapter mPhotoAdapter;

    public FetchItemsTask(String query, PhotoAdapter photoAdapter) {
        mQuery = query;
        mPhotoAdapter = photoAdapter;
    }

    @Override
    protected List<GalleryItem> doInBackground(Void... Void) {
        if (mQuery == null) {
            return new FlickrFetcher().fetchRecentPhotos(String.valueOf(FlickrApplication.getPageCount()));
        } else {
            return new FlickrFetcher().searchPhotos(mQuery, String.valueOf(FlickrApplication.getPageCount()));
        }
    }

    @Override
    protected void onPostExecute(List<GalleryItem> items) {
        DialogUtil.closeDialog();
        int page_count = FlickrApplication.getPageCount();
        if (page_count == 1) {
            mPhotoAdapter.addNewPhotosForNewSearch(items);
        } else {
            mPhotoAdapter.addNewPhotos(items);
        }
        page_count++;
        FlickrApplication.setPageCount(page_count);
    }
}
