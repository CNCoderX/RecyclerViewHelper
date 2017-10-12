package com.cncoderx.recyclerviewhelper.adapter;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author cncoderx
 */
public class ArrayAdapter<T> extends BaseAdapter implements Filterable {
    private final Object mLock = new Object();

    private final int mResource;

    private List<T> mObjects;

    private int mFieldId = 0;

    private boolean mNotifyOnChange = true;

    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    private ArrayList<T> mOriginalValues;
    private ArrayFilter mFilter;

    public ArrayAdapter(@LayoutRes int resource) {
        this(resource, 0, new ArrayList<T>());
    }

    public ArrayAdapter(@LayoutRes int resource, @IdRes int textViewResourceId) {
        this(resource, textViewResourceId, new ArrayList<T>());
    }

    public ArrayAdapter(@LayoutRes int resource, @NonNull T[] objects) {
        this(resource, 0, Arrays.asList(objects));
    }

    public ArrayAdapter(@LayoutRes int resource, @IdRes int textViewResourceId, @NonNull T[] objects) {
        this(resource, textViewResourceId, Arrays.asList(objects));
    }

    public ArrayAdapter(@LayoutRes int resource, @NonNull List<T> objects) {
        this(resource, 0, objects);
    }

    public ArrayAdapter(@LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<T> objects) {
        mResource = resource;
        mObjects = objects;
        mFieldId = textViewResourceId;
    }

    public void add(@Nullable T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            } else {
                mObjects.add(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(@NonNull Collection<? extends T> collection) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(collection);
            } else {
                mObjects.addAll(collection);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(T ... items) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.addAll(mOriginalValues, items);
            } else {
                Collections.addAll(mObjects, items);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void insert(@Nullable T object, int index) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void remove(@Nullable T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            } else {
                mObjects.remove(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                mObjects.clear();
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void sort(@NonNull Comparator<? super T> comparator) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
            } else {
                Collections.sort(mObjects, comparator);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public @Nullable T getItem(int position) {
        return mObjects.get(position);
    }

    public int getPosition(@Nullable T item) {
        return mObjects.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(mResource, parent, false);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        final TextView text;
        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) holder.itemView;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = holder.getView(mFieldId, TextView.class);

                if (text == null) {
                    throw new RuntimeException("Failed to find view with ID "
                            + holder.itemView.getResources().getResourceName(mFieldId)
                            + " in item layout");
                }
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        final T item = getItem(position);
        if (item instanceof CharSequence) {
            text.setText((CharSequence) item);
        } else {
            text.setText(item.toString());
        }
    }

    public static @NonNull ArrayAdapter<CharSequence> createFromResource(@NonNull Context context, @ArrayRes int textArrayResId, @LayoutRes int textViewResId) {
        final CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
        return new ArrayAdapter<>(textViewResId, strings);
    }

    @Override
    public @NonNull Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                final ArrayList<T> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                final ArrayList<T> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<T>) results.values;
            notifyDataSetChanged();
        }
    }
}

