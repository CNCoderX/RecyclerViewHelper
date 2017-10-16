package com.cncoderx.test.recyclerviewhelper.adapter;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.data.Album;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes;

import java.util.Collection;
import java.util.List;

/**
 * @author cncoderx
 */
public class AlbumSwipeAdapter extends ObjectAdapter<Album> implements SwipeItemMangerInterface, SwipeAdapterInterface {
    protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public AlbumSwipeAdapter(@LayoutRes int resource) {
        super(resource);
    }

    public AlbumSwipeAdapter(@LayoutRes int resource, Album... data) {
        super(resource, data);
    }

    public AlbumSwipeAdapter(@LayoutRes int resource, Collection<? extends Album> data) {
        super(resource, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, Album object, int position) {
        mItemManger.bindView(holder.itemView, holder.getLayoutPosition());
        holder.getView(R.id.cover, ImageView.class).setImageResource(object.getCover());
        holder.getView(R.id.name, TextView.class).setText(object.getName());
        holder.getView(R.id.price, TextView.class).setText(object.getPrice());
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }
}
