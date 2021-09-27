package com.cncoderx.test.recyclerviewhelper.adapter;

import androidx.annotation.LayoutRes;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.data.Album;

import java.util.Collection;

/**
 * @author cncoderx
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
    public void onBindViewHolder(BaseViewHolder holder, Album album, final int position) {
        ImageView ivCover = holder.getView(R.id.cover);
        ivCover.setImageResource(album.getCover());

        TextView ivName = holder.getView(R.id.name);
        ivName.setText(album.getName());

        TextView ivPrice = holder.getView(R.id.price);
        ivPrice.setText(album.getPrice());
    }
}
