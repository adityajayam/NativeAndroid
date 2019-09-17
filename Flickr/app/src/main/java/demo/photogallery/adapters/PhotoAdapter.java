package demo.photogallery.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import demo.photogallery.PhotoThreadPoolDownload;
import demo.photogallery.R;
import demo.photogallery.ThumbnailDownloadTask;
import demo.photogallery.databinding.ListItemGalleryBinding;
import demo.photogallery.model.GalleryItem;
import demo.photogallery.util.ThumbnailDownloader;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {
    private List<GalleryItem> mGalleryItems;
    private Context mContext;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;
    private static final String TAG = "PhotoAdapter";

    public PhotoAdapter(Context context, ThumbnailDownloader<PhotoHolder> thumbnailDownloader) {
        mGalleryItems = new ArrayList<>();
        mContext = context;
        mThumbnailDownloader = thumbnailDownloader;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ListItemGalleryBinding listItemGalleryBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_gallery, parent, false);
        return new PhotoHolder(listItemGalleryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        Log.e(TAG, String.valueOf(position));
        //If you are using the HandlerThread way to download images then uncomment the below code and comment the rest part like threadpool
        //executor and third party libraries
        //mThumbnailDownloader.queueThumbnail(holder, mGalleryItems.get(position).getmUrl());

        //For ThreadPool executor way we first need to create a task which downloads the photo and then pass that to the ThreadPool executor, then depending
        //on the free threads in the pool each and every task will be executed on a background thread.
        ThumbnailDownloadTask thumbnailDownloadTask = new ThumbnailDownloadTask(mContext, holder, mGalleryItems.get(position).getmUrl());
        PhotoThreadPoolDownload.getInstance().startDownload(thumbnailDownloadTask);

        //If using any third party library like picasso or glide no need of the below code.
        /*holder.listItemGalleryBinding.itemImageView.setImageDrawable(mGalleryItems.get(position).getmDrawable());
        holder.listItemGalleryBinding.setPosition(position);
        holder.listItemGalleryBinding.getRoot().setTag(position);*/

        //Third party libraries to download the images.
        //Picasso.get().load(mGalleryItems.get(position).getmUrl()).into(holder.listItemGalleryBinding.itemImageView);
        //Glide.with(mContext).load(mGalleryItems.get(position).getmUrl()).into(holder.listItemGalleryBinding.itemImageView);
    }

    @Override
    public int getItemCount() {
        return mGalleryItems.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {
        public ListItemGalleryBinding listItemGalleryBinding;

        public PhotoHolder(ListItemGalleryBinding listItemGalleryBinding) {
            super(listItemGalleryBinding.getRoot());
            this.listItemGalleryBinding = listItemGalleryBinding;
        }
    }

    public void addNewPhotos(List<GalleryItem> galleryItems) {
        mGalleryItems.addAll(galleryItems);
        notifyDataSetChanged();
    }

    public void addNewPhotosForNewSearch(List<GalleryItem> galleryItems) {
        mGalleryItems.clear();
        mGalleryItems.addAll(galleryItems);
        notifyDataSetChanged();
    }
}
