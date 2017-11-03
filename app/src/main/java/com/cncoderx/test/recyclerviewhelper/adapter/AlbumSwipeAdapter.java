package com.cncoderx.test.recyclerviewhelper.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.SwipeAdapter;
import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.data.Album;

import java.util.Collection;

/**
 * @author cncoderx
 */
public class AlbumSwipeAdapter extends SwipeAdapter<Album> {

    public AlbumSwipeAdapter(@LayoutRes int resource) {
        super(resource);
    }

    public AlbumSwipeAdapter(@LayoutRes int resource, @NonNull Album[] objects) {
        super(resource, objects);
    }

    public AlbumSwipeAdapter(@LayoutRes int resource, @NonNull Collection<? extends Album> objects) {
        super(resource, objects);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, Album object, final int position) {
        holder.getView(R.id.cover, ImageView.class).setImageResource(object.getCover());
        holder.getView(R.id.name, TextView.class).setText(object.getName());
        holder.getView(R.id.price, TextView.class).setText(object.getPrice());
        holder.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });
    }
}
