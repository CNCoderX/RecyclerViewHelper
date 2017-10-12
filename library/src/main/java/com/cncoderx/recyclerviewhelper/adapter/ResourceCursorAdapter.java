package com.cncoderx.recyclerviewhelper.adapter;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author cncoderx
 */
public abstract class ResourceCursorAdapter extends CursorAdapter {
    private int mLayout;

    @Deprecated
    public ResourceCursorAdapter(int layout, Cursor c) {
        super(c);
        mLayout = layout;
    }

    public ResourceCursorAdapter(int layout, Cursor c, boolean autoRequery) {
        super(c, autoRequery);
        mLayout = layout;
    }

    public ResourceCursorAdapter(int layout, Cursor c, int flags) {
        super(c, flags);
        mLayout = layout;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(mLayout, parent, false);
    }

    public void setViewResource(int layout) {
        mLayout = layout;
    }
}

