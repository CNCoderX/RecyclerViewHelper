package com.cncoderx.recyclerviewhelper.listener;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * @author cncoderx
 */
public interface OnChildClickListener {
    void onChildClick(RecyclerView parent, View v, int groupPosition,
                         int childPosition, long id);
}
