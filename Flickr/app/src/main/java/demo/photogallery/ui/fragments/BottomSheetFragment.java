package demo.photogallery.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import demo.photogallery.interfaces.ImageClickInterface;

public class BottomSheetFragment extends BottomSheetDialogFragment implements ImageClickInterface {

    private static final String TAG = "BottomSheetFragment";
    private BottomSheetBinding bottomSheetBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        bottomSheetBinding = DataBindingUtil.inflate(layoutInflater, R.layout.bottom_sheet, viewGroup, false);
        bottomSheetBinding.imageActionListView.setAdapter(new ImageActionAdapter(getActivity(), this));
        bottomSheetBinding.imagePreview.setImageDrawable(FlickrApplication.getSelectedGalleryItem().getmDrawable());
        return bottomSheetBinding.getRoot();
    }

    /**
     * Method to share an image.
     */
    private void shareImage() {
        Intent shareImageIntent = new Intent();
        shareImageIntent.setAction(Intent.ACTION_SEND);
        shareImageIntent.setType("image/*");
        shareImageIntent.setPackage("com.whatsapp");
        shareImageIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.e(TAG, "Selected image path : " + FlickrApplication.getSelectedGalleryItem().getmUrl());
        Uri uriToImage = Uri.parse(FlickrApplication.getSelectedGalleryItem().getmUrl());
        Log.e(TAG, uriToImage.getPath());
        shareImageIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        startActivity(Intent.createChooser(shareImageIntent, "Share Image"));
    }

    @Override
    public void onClickImage(int position) {
        Log.e(TAG, "position:" + position);
        switch (position) {
            case 0:
                //TODO call share function
                shareImage();
                break;
            case 1:
                //TODO call copy link function
                break;
            case 2:
                //TODO call download image function
                break;
            default:
                break;
        }
    }
}
