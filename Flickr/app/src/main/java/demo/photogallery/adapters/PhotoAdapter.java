package demo.photogallery.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import demo.photogallery.R;
import demo.photogallery.databinding.ListItemGalleryBinding;
import demo.photogallery.model.GalleryItem;
import demo.photogallery.util.ThumbnailDownloader;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {
    private List<GalleryItem> mGalleryItems;
    private Context mContext;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

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
        mThumbnailDownloader.queueThumbnail(holder, mGalleryItems.get(position).getmUrl());
        holder.listItemGalleryBinding.itemImageView.setImageDrawable(mGalleryItems.get(position).getmDrawable());
        holder.listItemGalleryBinding.setPosition(position);
        holder.listItemGalleryBinding.getRoot().setTag(position);
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