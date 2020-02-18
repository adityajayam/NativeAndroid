package demo.photogallery.ui.fragments;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import demo.photogallery.FlickrApplication;
import demo.photogallery.R;
import demo.photogallery.adapters.ImageActionAdapter;
import demo.photogallery.databinding.BottomSheetBinding;
import demo.photogallery.interfaces.ImageClickInterface;


public class BottomSheetFragment extends BottomSheetDialogFragment implements ImageClickInterface {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        BottomSheetBinding bottomSheetBinding = DataBindingUtil.inflate(layoutInflater, R.layout.bottom_sheet, viewGroup, false);
        bottomSheetBinding.imageActionListView.setAdapter(new ImageActionAdapter(getActivity(), this));
        bottomSheetBinding.imagePreview.setImageDrawable(FlickrApplication.getSelectedGalleryItem().getmDrawable());
        return bottomSheetBinding.getRoot();
    }

    private void copyLinkToClipBoard() {
        String url = FlickrApplication.getSelectedGalleryItem().getmUrl();
        if (getContext() != null) {
            ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Copied Data", url);
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Url Copied!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Something went wrong. Please Try Again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void downloadImageToExternalStorage() {
        if (getContext() != null) {
            Uri uri = Uri.parse(FlickrApplication.getSelectedGalleryItem().getmUrl());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FlickrApplication.getSelectedGalleryItem().getmUrl());
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
            } else {
                Toast.makeText(getContext(), "Something went wrong. Please Try Again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareImageViaPicasso() {
        Picasso.get().load(FlickrApplication.getSelectedGalleryItem().getmUrl()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
               /* //ToDo:use FileProvider to share image.
                File rootDirectory = new File(Environment.DIRECTORY_DOWNLOADS, ".tempImages");
                File imageFile = new File(rootDirectory, System.currentTimeMillis() + ".jpg");
                FileOutputStream out;
                try {
                    out = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                }*/


                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, System.currentTimeMillis() + ".jpg", "Ignore Madi!!");
                Intent shareImageIntent = new Intent();
                shareImageIntent.setAction(Intent.ACTION_SEND);
                shareImageIntent.setType("image/*");
                shareImageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                shareImageIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareImageIntent, "Ignore Madi!!"));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Toast.makeText(getContext(), "Something went wrong. Please Try Again!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    @Override
    public void onClickImage(int position) {
        switch (position) {
            case 0:
                shareImageViaPicasso();
                break;
            case 1:
                copyLinkToClipBoard();
                break;
            case 2:
                downloadImageToExternalStorage();
                break;
            default:
                break;
        }
    }
}
