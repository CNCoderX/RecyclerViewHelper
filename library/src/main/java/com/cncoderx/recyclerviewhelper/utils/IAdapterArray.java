package com.cncoderx.recyclerviewhelper.utils;

import android.support.v7.widget.RecyclerView;

/**
 * @author cncoderx
 */
public interface IAdapterArray {
    boolean isNotifyOnChange();
    void setNotifyOnChange(boolean notify);
    int getPositionOffset();
    void setPositionOffset(int offset);
    RecyclerView.Adapter getAdapter();
}
