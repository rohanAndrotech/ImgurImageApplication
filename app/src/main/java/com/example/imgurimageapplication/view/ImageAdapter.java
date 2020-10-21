package com.example.imgurimageapplication.view;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;


public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> stringList;


    public ImageAdapter(Context context, List<String> imageUrlsList) {
        this.context = context;
        this.stringList = imageUrlsList;
    }

    @Override
    public int getCount() {
        return stringList.size();
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
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, 400));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageView = (ImageView) convertView;
        }
        Uri uri = Uri.parse(stringList.get(position));
        Glide.with(context).load(uri).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        return imageView;
    }
}
