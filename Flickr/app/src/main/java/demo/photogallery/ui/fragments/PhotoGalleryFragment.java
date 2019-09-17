package demo.photogallery.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import demo.photogallery.FlickrFetcher;
import demo.photogallery.R;
import demo.photogallery.adapters.PhotoAdapter;
import demo.photogallery.databinding.FragmentPhotoGalleryBinding;
import demo.photogallery.model.GalleryItem;
import demo.photogallery.services.PollService;
import demo.photogallery.tasks.FetchItemsTask;
import demo.photogallery.util.DialogUtil;
import demo.photogallery.util.QueryPreferences;
import demo.photogallery.util.ThumbnailDownloader;
import demo.photogallery.viewmodel.PhotoListViewModel;

public class PhotoGalleryFragment extends VisibleFragment {
    private static String TAG = "PhotoGalleryFragment";
    private FragmentPhotoGalleryBinding fragmentPhotoGalleryBinding;
    private PhotoAdapter photoAdapter;
    //private ThumbnailDownloader<PhotoAdapter.PhotoHolder> mThumbnailDownloader;
    //private static Dialog progressDialog;
    private PhotoListViewModel photoListViewModel;
    private Activity mActivity;

    public static Fragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mActivity = getActivity();
        photoListViewModel = new PhotoListViewModel();
        //If you are using the HandlerThread way to download images then uncomment the below code and comment the rest part like threadpool
        //executor and third party libraries
/*
        StrictMode.enableDefaults();
        TrafficStats.setThreadStatsTag(mActivity.getApplication().getApplicationInfo().uid);
*/
        //UI thread has a Looper created for it implicitly
        //Create the Handler. It will implicitly bind to the Looper
        //that is internally created for this thread (since it is the UI thread)
        //Handler responseHandler = new Handler();
/*
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(new ThumbnailDownloader.ThumbnailDownloadListener<PhotoAdapter.PhotoHolder>() {
            @Override
            public void onThumbnailDownloaded(PhotoAdapter.PhotoHolder photoHolder, Bitmap thumbnail) {
                Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                photoHolder.listItemGalleryBinding.itemImageView.setImageDrawable(drawable);
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");
*/
    }

    private void updateItems() {
        String query = QueryPreferences.getStoredQuery(mActivity);
        DialogUtil.showProgressDialogBar(mActivity, "Downloading images...");
        new FetchItemsTask(query, photoAdapter).execute();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "QueryTextSubmit: " + query);
                hideKeyboard(mActivity);
                QueryPreferences.setStoredQuery(mActivity, query);
                updateItems();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "QueryTextChange: " + newText);
                return false;
            }
        });
        searchView.setOnSearchClickListener(v -> {
            String query = QueryPreferences.getStoredQuery(mActivity);
            searchView.setQuery(query, false);
        });
        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (PollService.isServiceAlarmOn(mActivity)) {
            toggleItem.setTitle(R.string.stop_polling);
        } else {
            toggleItem.setTitle(R.string.start_polling);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);
        switch (menuItem.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(mActivity, null);

                updateItems();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(mActivity);
                PollService.setServiceAlarm(mActivity, shouldStartAlarm);
                mActivity.invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        fragmentPhotoGalleryBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_photo_gallery, viewGroup, false);
        fragmentPhotoGalleryBinding.photoRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        setupAdapter();
        updateItems();
        fragmentPhotoGalleryBinding.photoRecyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (!fragmentPhotoGalleryBinding.photoRecyclerView.canScrollVertically(1)) {
                updateItems();
                Log.e(TAG, "End of scroll");
            }
        });
        fragmentPhotoGalleryBinding.setPhotoListViewModel(photoListViewModel);
        initObservables();
        return fragmentPhotoGalleryBinding.getRoot();
    }

    public void initObservables() {
        photoListViewModel.getMutableLiveData().observe(this, status -> Log.e(TAG, "Selected Position: " + status));
    }

    private void setupAdapter() {
        if (isAdded()) {
            photoAdapter = new PhotoAdapter(mActivity, null);//mThumbnailDownloader);
            fragmentPhotoGalleryBinding.photoRecyclerView.setAdapter(photoAdapter);
        }
    }

}
