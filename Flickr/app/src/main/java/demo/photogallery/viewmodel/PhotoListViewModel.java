package demo.photogallery.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PhotoListViewModel extends ViewModel {
    private static final String TAG = "PhotoListViewModel";
    private final MutableLiveData<Integer> mutableLiveData;

    public PhotoListViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }

    public void onImageSelect(int position) {
        Log.e(TAG, "Selected Image Position" + position);
        mutableLiveData.setValue(position);
    }

    public MutableLiveData<Integer> getMutableLiveData() {
        return mutableLiveData;
    }
}
