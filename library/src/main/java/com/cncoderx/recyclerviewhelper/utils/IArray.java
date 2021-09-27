package com.cncoderx.recyclerviewhelper.utils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.Comparator;

/**
 * @author cncoderx
 */

public interface IArray<T> {
    boolean isEmpty();
    int size();
    T get(@IntRange(from = 0) int index);
    int indexOf(@NonNull T object);
    void set(@IntRange(from = 0) int index, @NonNull T object);
    void add(@NonNull T object);
    void add(@IntRange(from = 0) int index, @NonNull T object);
    void addAll(@NonNull Collection<? extends T> objects);
    void addAll(@IntRange(from = 0) int index, @NonNull Collection<? extends T> objects);
    void remove(@IntRange(from = 0) int index);
    void remove(@NonNull T object);
    void removeRange(@IntRange(from = 0) int fromIndex, @IntRange(from = 0) int toIndex);
    void swap(@IntRange(from = 0) int index, @IntRange(from = 0) int index2);
    void sort(@NonNull Comparator<? super T> comparator);
    void clear();

    public static interface Callback {
        void onChanged(int index);
        void onAdded(int index);
        void onRemoved(int index);
        void onSwapped(int index, int index2);

        void onChanged();
        void onRangeAdded(int index, int size);
        void onRangeRemoved(int index, int size);
    }
}
