package demo.photogallery.threadPoolAndTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;

import demo.photogallery.FlickrFetcher;
import demo.photogallery.adapters.PhotoAdapter;

public class ThumbnailDownloadTask<T> implements Runnable {
    private static final String TAG = "ThumbnailDownloadTask";
    private String url;
    private PhotoAdapter.PhotoHolder holder;
    private Context mContext;

    public ThumbnailDownloadTask(Context context, PhotoAdapter.PhotoHolder holder, String getmUrl) {
        url = getmUrl;
        this.holder = holder;
        mContext = context;
    }

    @Override
    public void run() {
        handleRequest();
    }

    private void handleRequest() {
        final Bitmap bitmap;
        if (url == null) {
            return;
        }
        try {
            byte[] bitmapBytes = new FlickrFetcher().getUrlBytes(url);
            bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");
            ((Activity) mContext).runOnUiThread(() -> {
                Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
                holder.listItemGalleryBinding.itemImageView.setImageDrawable(drawable);
            });
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }
}
