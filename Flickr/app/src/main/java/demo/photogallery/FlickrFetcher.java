package demo.photogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import demo.photogallery.model.GalleryItem;

public class FlickrFetcher {
    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = "8ab8b56dcaf60672b80b4474d699d80f";
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private final Uri END_POINT = Uri.parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build();

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream inputStream = httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage() + "with" + urlSpec);
            }
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            out.close();
            return out.toByteArray();
        } finally {
            httpURLConnection.disconnect();
        }
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private List<GalleryItem> downloadGalleryItems(String url) {
        List<GalleryItem> items = new ArrayList<>();
        Log.i(TAG, "Url: " + url);
        try {
            String jsonString = getUrlString(url);
            parseItems(items, new JSONObject(jsonString));
            Log.i(TAG, "Received JSON: " + jsonString);
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch items", e);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON", e);
        }
        return items;
    }

    public List<GalleryItem> fetchRecentPhotos(String pageNumber) {
        String url = buildUrl(FETCH_RECENTS_METHOD, null, pageNumber);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> searchPhotos(String query, String pageNumber) {
        String url = buildUrl(SEARCH_METHOD, query, pageNumber);
        return downloadGalleryItems(url);
    }

    private String buildUrl(String method, String query, String pageNumber) {
        Uri.Builder uriBuilder = END_POINT.buildUpon()
                .appendQueryParameter("method", method);
        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("text", query);
        }
        uriBuilder.appendQueryParameter("page", pageNumber);
        return uriBuilder.build().toString();
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws JSONException {
        JSONObject photosObject = jsonBody.getJSONObject("photos");
        JSONArray photoArray = photosObject.getJSONArray("photo");
        for (int i = 0; i < photoArray.length(); i++) {
            JSONObject photoJsonObject = photoArray.getJSONObject(i);
            GalleryItem galleryItem = new GalleryItem();
            galleryItem.setmId(photoJsonObject.getString("id"));
            galleryItem.setmCaption(photoJsonObject.getString("title"));
            if (!photoJsonObject.has("url_s")) {
                continue;
            }
            galleryItem.setmUrl(photoJsonObject.getString("url_s"));
            galleryItem.setmOwner(photoJsonObject.getString("owner"));
            items.add(galleryItem);
        }
    }
}
