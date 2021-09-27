package com.cncoderx.recyclerviewhelper.utils;

import androidx.recyclerview.widget.RecyclerView;

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
