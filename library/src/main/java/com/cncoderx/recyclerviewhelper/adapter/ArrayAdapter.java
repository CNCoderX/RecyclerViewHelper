package com.cncoderx.recyclerviewhelper.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.Collection;

/**
 * @author cncoderx
 */
public class ArrayAdapter<T> extends ObjectAdapter<T> {
    private int mFieldId = android.R.id.text1;

    public ArrayAdapter(@LayoutRes int resource) {
        super(resource);
    }

    public ArrayAdapter(@LayoutRes int resource, @IdRes int textViewResourceId) {
        super(resource);
        mFieldId = textViewResourceId;
    }

    public ArrayAdapter(@LayoutRes int resource, @NonNull T... objects) {
        super(resource, objects);
    }

    public ArrayAdapter(@LayoutRes int resource, @NonNull Collection<? extends T> objects) {
        super(resource, objects);
    }

    public ArrayAdapter(@LayoutRes int resource, @IdRes int textViewResourceId, @NonNull T... objects) {
        super(resource, objects);
        mFieldId = textViewResourceId;
    }

    public ArrayAdapter(@LayoutRes int resource, @IdRes int textViewResourceId, @NonNull Collection<? extends T> objects) {
        super(resource, objects);
        mFieldId = textViewResourceId;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, T object, int position) {
        TextView text = holder.getView(mFieldId, TextView.class);
        if (object instanceof CharSequence) {
            text.setText((CharSequence) object);
        } else {
            text.setText(object.toString());
        }
    }
}

