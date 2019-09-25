package demo.photogallery.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PhotoListViewModel extends ViewModel {
    private final MutableLiveData<Integer> mutableLiveData;

    public PhotoListViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }

    public void onImageSelect(int position) {
        mutableLiveData.setValue(position);
    }

    public MutableLiveData<Integer> getMutableLiveData() {
        return mutableLiveData;
    }
}
