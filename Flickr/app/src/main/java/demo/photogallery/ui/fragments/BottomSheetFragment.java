package demo.photogallery.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import demo.photogallery.FlickrApplication;
import demo.photogallery.R;
import demo.photogallery.adapters.ImageActionAdapter;
import demo.photogallery.databinding.BottomSheetBinding;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private ListView imageActionListView;
    private ImageView imagePreview;
    private BottomSheetBinding bottomSheetBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        bottomSheetBinding = DataBindingUtil.inflate(layoutInflater, R.layout.bottom_sheet, viewGroup, false);
        bottomSheetBinding.imageActionListView.setAdapter(new ImageActionAdapter(getActivity()));
        bottomSheetBinding.imagePreview.setImageDrawable(FlickrApplication.getSelectedGalleryItem().getmDrawable());
        return bottomSheetBinding.getRoot();
    }
}
