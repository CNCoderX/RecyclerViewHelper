package com.cncoderx.recyclerviewhelper.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/**
 * @author cncoderx
 */
public class AdapterArray<T> extends Array<T> implements IAdapterArray {
    private final RecyclerView.Adapter mAdapter;
    private boolean mNotifyOnChange = true;
    private int mPositionOffset = 0;

    public AdapterArray(@NonNull RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    public AdapterArray(@NonNull RecyclerView.Adapter adapter, T... objects) {
        mAdapter = adapter;
        addAll(Arrays.asList(objects));
    }

    public AdapterArray(@NonNull RecyclerView.Adapter adapter, Collection<? extends T> objects) {
        mAdapter = adapter;
        addAll(objects);
    }

    public void set(int index, @NonNull T object) {
        super.set(index, object);
        if (isNotifyOnChange()) {
            int position = getPosition(index);
            mAdapter.notifyItemChanged(position);
        }
    }

    public void add(@NonNull T object) {
        super.add(object);
        if (isNotifyOnChange()) {
            int position = getPosition(size() - 1);
            mAdapter.notifyItemInserted(position);
        }
    }

    public void add(int index, @NonNull T object) {
        super.add(index, object);
        if (isNotifyOnChange()) {
            int position = getPosition(index);
            mAdapter.notifyItemInserted(position);
        }
    }

    public void addAll(@NonNull Collection<? extends T> objects) {
        super.addAll(objects);
        int size = objects.size();
        int subSize = size() - size;
        if (isNotifyOnChange()) {
            if (subSize == 0) {
                mAdapter.notifyDataSetChanged();
            } else {
                int position = getPosition(subSize);
                mAdapter.notifyItemRangeInserted(position, size);
            }
        }
    }

    public void addAll(int index, @NonNull Collection<? extends T> objects) {
        super.addAll(index, objects);
        int size = objects.size();
        int subSize = size() - size;
        if (isNotifyOnChange()) {
            if (subSize == 0) {
                mAdapter.notifyDataSetChanged();
            } else {
                int position = getPosition(index);
                mAdapter.notifyItemRangeInserted(position, size);
            }
        }
    }

    public void remove(int index) {
        super.remove(index);
        if (isNotifyOnChange()) {
            int position = getPosition(index);
            int notifyCount = size() - position;
            mAdapter.notifyItemRemoved(position);
            mAdapter.notifyItemRangeChanged(position, notifyCount);
        }
    }

    public void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        if (isNotifyOnChange()) {
            int position = getPosition(fromIndex);
            int removeCount = toIndex - fromIndex;
            int notifyCount = size() - position;
            mAdapter.notifyItemRangeRemoved(position, removeCount);
            mAdapter.notifyItemRangeChanged(position, notifyCount);
        }
    }

    @Override
    public void sort(@NonNull Comparator<? super T> comparator) {
        super.sort(comparator);
        if (isNotifyOnChange()) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void clear() {
        super.clear();
        if (isNotifyOnChange()) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private int getPosition(int index) {
        return index + mPositionOffset;
    }

    @Override
    public boolean isNotifyOnChange() {
        return mNotifyOnChange;
    }

    @Override
    public void setNotifyOnChange(boolean notify) {
        mNotifyOnChange = notify;
    }

    public int getPositionOffset() {
        return mPositionOffset;
    }

    public void setPositionOffset(int positionOffset) {
        mPositionOffset = positionOffset;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }
}
