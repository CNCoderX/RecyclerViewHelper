package com.cncoderx.recyclerviewhelper.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author cncoderx
 */
public class Array<T> implements IArray<T> {

    private ArrayList<T> mArray = new ArrayList<>();

    private Callback mCallback;

    public Callback getCallback() {
        return mCallback;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
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
        if (mCallback != null) {
            mCallback.onChanged(index);
        }
    }

    @Override
    public void add(@NonNull T object) {
        int index = mArray.size();
        mArray.add(object);
        if (mCallback != null) {
            mCallback.onAdded(index);
        }
    }

    @Override
    public void add(int index, @NonNull T object) {
        mArray.add(index, object);
        if (mCallback != null) {
            mCallback.onAdded(index);
        }
    }

    @Override
    public void addAll(@NonNull Collection<? extends T> objects) {
        int index = mArray.size();
        mArray.addAll(objects);
        if (mCallback != null) {
            int size = objects.size();
            mCallback.onRangeAdded(index, size);
        }
    }

    @Override
    public void addAll(int index, @NonNull Collection<? extends T> objects) {
        mArray.addAll(index, objects);
        if (mCallback != null) {
            int size = objects.size();
            mCallback.onRangeAdded(index, size);
        }
    }

    @Override
    public void remove(int index) {
        mArray.remove(index);
        if (mCallback != null) {
            mCallback.onRemoved(index);
        }
    }

    @Override
    public void remove(@NonNull T object) {
        int index = mArray.indexOf(object);
        if (index != -1) remove(index);
    }

    @Override
    public void removeRange(int fromIndex, int toIndex) {
        mArray.subList(fromIndex, toIndex).clear();
        if (mCallback != null) {
            int size = toIndex - fromIndex;
            mCallback.onRangeRemoved(fromIndex, size);
        }
    }

    @Override
    public void swap(int index, int index2) {
        if (index == index2)
            return;
        T object = mArray.get(index);
        T object2 = mArray.get(index2);
        mArray.set(index, object2);
        mArray.set(index2, object);
        if (mCallback != null) {
            mCallback.onSwapped(index, index2);
        }
    }

    @Override
    public void sort(@NonNull Comparator<? super T> comparator) {
        Collections.sort(mArray, comparator);
        if (mCallback != null) {
            mCallback.onChanged();
        }
    }

    @Override
    public void clear() {
        mArray.clear();
        if (mCallback != null) {
            mCallback.onChanged();
        }
    }
}
