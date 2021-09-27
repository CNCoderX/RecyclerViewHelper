package com.cncoderx.test.recyclerviewhelper.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
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
    public void onBindViewHolder(BaseViewHolder holder, Album album, final int position) {
        ImageView ivCover = holder.getView(R.id.cover);
        ivCover.setImageResource(album.getCover());

        TextView ivName = holder.getView(R.id.name);
        ivName.setText(album.getName());

        TextView ivPrice = holder.getView(R.id.price);
        ivPrice.setText(album.getPrice());

        View vDelete = holder.getView(R.id.delete);
        vDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });
    }
}
