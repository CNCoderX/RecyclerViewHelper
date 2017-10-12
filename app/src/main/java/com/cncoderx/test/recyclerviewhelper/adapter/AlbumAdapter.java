package com.cncoderx.test.recyclerviewhelper.adapter;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.data.Album;

import java.util.Collection;

/**
 * Created by admin on 2017/9/29.
 */
public class AlbumAdapter extends ObjectAdapter<Album> {

    public AlbumAdapter(@LayoutRes int resource) {
        super(resource);
    }

    public AlbumAdapter(@LayoutRes int resource, Album... data) {
        super(resource, data);
    }

    public AlbumAdapter(@LayoutRes int resource, Collection<? extends Album> data) {
        super(resource, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, Album object, int position) {
        holder.getView(R.id.cover, ImageView.class).setImageResource(object.getCover());
        holder.getView(R.id.name, TextView.class).setText(object.getName());
        holder.getView(R.id.price, TextView.class).setText(object.getPrice());
    }
}
