package com.cncoderx.recyclerviewhelper.adapter;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author cncoderx
 */
public abstract class ObjectAdapter<T> extends BaseAdapter {
    private @LayoutRes int mResource;
    private ArrayList<T> mData = new ArrayList<>();

    private int mPositionOffset = 0;
    private boolean isNotifiable = true;

    public ObjectAdapter(@LayoutRes int resource) {
        mResource = resource;
    }

    public ObjectAdapter(@LayoutRes int resource, T... data) {
        mResource = resource;
        Collections.addAll(mData, data);
    }

    public ObjectAdapter(@LayoutRes int resource, Collection<? extends T> data) {
        mResource = resource;
        mData.addAll(data);
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(mResource, parent, false);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        final T object = get(position);
        onBindViewHolder(holder, object, position);
    }

    public abstract void onBindViewHolder(BaseViewHolder holder, T object, int position);

    @Override
    public int getItemCount() {
        return size();
    }

    public int size() {
        return mData.size();
    }

    public T get(int index) {
        return mData.get(index);
    }

    public void set(int index, T object) {
        mData.set(index, object);
        if (isNotifiable) {
            int position = getPosition(index);
            notifyItemChanged(position);
        }
    }

    public void add(T object) {
        int lastIndex = mData.size();
        mData.add(object);
        if (isNotifiable) {
            int position = getPosition(lastIndex);
            notifyItemInserted(position);
        }
    }

    public void add(int index, T object) {
        mData.add(index, object);
        if (isNotifiable) {
            int position = getPosition(index);
            notifyItemInserted(position);
        }
    }

    public void addAll(Collection<? extends T> objects) {
        int lastIndex = mData.size();
        mData.addAll(objects);
        if (isNotifiable) {
            if (lastIndex == 0) {
                notifyDataSetChanged();
            } else {
                int position = getPosition(lastIndex);
                notifyItemRangeInserted(position, objects.size());
            }
        }
    }

    public void addAll(int index, Collection<? extends T> objects) {
        int lastIndex = mData.size();
        mData.addAll(index, objects);
        if (isNotifiable) {
            if (lastIndex == 0) {
                notifyDataSetChanged();
            } else {
                int position = getPosition(index);
                notifyItemRangeInserted(position, objects.size());
            }
        }
    }

    public void remove(int index) {
        mData.remove(index);
        if (isNotifiable) {
            int position = getPosition(index);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size() - position);
        }
    }

    public void remove(T object) {
        int index = mData.indexOf(object);
        if (index != -1) remove(index);
    }

    public void removeRange(int fromIndex, int toIndex) {
        mData.subList(fromIndex, toIndex).clear();
        if (isNotifiable) {
            int position = getPosition(fromIndex);
            int count = toIndex - fromIndex;
            notifyItemRangeRemoved(position, count);
            notifyItemRangeChanged(position, mData.size() - position);
        }
    }

    public void clear() {
        mData.clear();
        if (isNotifiable) {
            notifyDataSetChanged();
        }
    }

    private int getPosition(int index) {
        return index + mPositionOffset;
    }

    public int getPositionOffset() {
        return mPositionOffset;
    }

    public void setPositionOffset(int positionOffset) {
        mPositionOffset = positionOffset;
    }

    public boolean isNotifiable() {
        return isNotifiable;
    }

    public void setNotifiable(boolean notifiable) {
        isNotifiable = notifiable;
    }
}
