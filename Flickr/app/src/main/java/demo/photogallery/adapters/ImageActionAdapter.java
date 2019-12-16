package demo.photogallery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import demo.photogallery.R;
import demo.photogallery.interfaces.ImageClickInterface;

public class ImageActionAdapter extends BaseAdapter {

    private Context mContext;
    private ImageClickInterface mImageClickInterface;

    private String[] actionNames = new String[]{"Share", "Get Link", "Download"};

    private int[] actionDrawables = new int[]{R.drawable.ic_action_share, R.drawable.ic_action_copy_link, R.drawable.ic_action_download};

    public ImageActionAdapter(Context context, ImageClickInterface imageClickInterface) {
        mContext = context;
        mImageClickInterface = imageClickInterface;
    }

    @Override
    public int getCount() {
        return actionNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageActionViewHolder imageActionViewHolder;
        if (convertView == null) {
            imageActionViewHolder = new ImageActionViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.image_action_row_item, parent, false);
            imageActionViewHolder.imageActionRowItemLayout = convertView.findViewById(R.id.image_action_row_item_layout);
            imageActionViewHolder.actionItemImageView = convertView.findViewById(R.id.action_item_image);
            imageActionViewHolder.actionItemName = convertView.findViewById(R.id.action_item_name);
            convertView.setTag(imageActionViewHolder);
        } else {
            imageActionViewHolder = (ImageActionViewHolder) convertView.getTag();
        }
        imageActionViewHolder.imageActionRowItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageClickInterface.onClickImage(position);
            }
        });
        imageActionViewHolder.actionItemName.setText(actionNames[position]);
        imageActionViewHolder.actionItemImageView.setImageResource(actionDrawables[position]);
        return convertView;
    }

    class ImageActionViewHolder {
        ConstraintLayout imageActionRowItemLayout;
        ImageView actionItemImageView;
        TextView actionItemName;
    }
}
