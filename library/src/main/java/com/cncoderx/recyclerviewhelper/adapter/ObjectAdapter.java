package com.cncoderx.recyclerviewhelper.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoderx.recyclerviewhelper.utils.Array;
import com.cncoderx.recyclerviewhelper.utils.IArray;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/**
 * @author cncoderx
 */
public abstract class ObjectAdapter<T> extends BaseAdapter implements IArray<T>, IArray.Callback {
    private @LayoutRes int mResource;
    private final Array<T> mArray = new Array<>();

    private boolean mNotifyOnChange = true;
    private int mPositionOffset = 0;

    public ObjectAdapter(@LayoutRes int resource) {
        mResource = resource;
        mArray.setCallback(this);
    }

    @SafeVarargs
    public ObjectAdapter(@LayoutRes int resource, @NonNull T... objects) {
        mResource = resource;
        mArray.addAll(Arrays.asList(objects));
        mArray.setCallback(this);
    }

    public ObjectAdapter(@LayoutRes int resource, @NonNull Collection<? extends T> objects) {
        mResource = resource;
        mArray.addAll(objects);
        mArray.setCallback(this);
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return mArray.isEmpty();
    }

    @Override
    public int size() {
        return mArray.size();
    }

    @Override
    public T get(int index) {
        return mArray.get(index);
    }

    @Override
    public int indexOf(@NonNull T object) {
        return mArray.indexOf(object);
    }

    @Override
    public void set(int index, @NonNull T object) {
        mArray.set(index, object);
    }

    @Override
    public void add(@NonNull T object) {
        mArray.add(object);
    }

    @Override
    public void add(int index, @NonNull T object) {
        mArray.add(index, object);
    }

    @Override
    public void addAll(@NonNull Collection<? extends T> objects) {
        mArray.addAll(objects);
    }

    @Override
    public void addAll(int index, @NonNull Collection<? extends T> objects) {
        mArray.addAll(index, objects);
    }

    @Override
    public void remove(int index) {
        mArray.remove(index);
    }

    @Override
    public void remove(@NonNull T object) {
        mArray.remove(object);
    }

    @Override
    public void removeRange(int fromIndex, int toIndex) {
        mArray.removeRange(fromIndex, toIndex);
    }

    @Override
    public void swap(int index, int index2) {
        mArray.swap(index, index2);
    }

    @Override
    public void sort(@NonNull Comparator<? super T> comparator) {
        mArray.sort(comparator);
    }

    @Override
    public void clear() {
        mArray.clear();
    }

    @Override
    public void onChanged(int index) {
        if (isNotifyOnChange()) {
            int position = getPosition(index);
            notifyItemChanged(position);
        }
    }

    @Override
    public void onAdded(int index) {
        if (isNotifyOnChange()) {
            int position = getPosition(index);
            notifyItemInserted(position);
        }
    }

    @Override
    public void onRemoved(int index) {
        if (isNotifyOnChange()) {
            int position = getPosition(index);
            int notifyCount = size() - index;
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notifyCount);
        }
    }

    @Override
    public void onSwapped(int index, int index2) {
        if (isNotifyOnChange()) {
            int position;
            int position2;
            if (index < index2) {
                position = getPosition(index);
                position2 = getPosition(index2);
            } else {
                position = getPosition(index2);
                position2 = getPosition(index);
            }
            notifyItemMoved(position, position2);
            notifyItemMoved(position2 - 1, position);
        }
    }

    @Override
    public void onChanged() {
        if (isNotifyOnChange()) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void onRangeAdded(int index, int size) {
        if (isNotifyOnChange()) {
            if (size() - size == 0) {
                notifyDataSetChanged();
            } else {
                int position = getPosition(index);
                notifyItemRangeInserted(position, size);
            }
        }
    }

    @Override
    public void onRangeRemoved(int index, int size) {
        if (isNotifyOnChange()) {
            int position = getPosition(index);
            int notifyCount = size() - index;
            notifyItemRangeRemoved(position, size);
            notifyItemRangeChanged(position, notifyCount);
        }
    }

    private int getPosition(int index) {
        return index + mPositionOffset;
    }

    public boolean isNotifyOnChange() {
        return mNotifyOnChange;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    public int getPositionOffset() {
        return mPositionOffset;
    }

    public void setPositionOffset(int positionOffset) {
        mPositionOffset = positionOffset;
    }
}
