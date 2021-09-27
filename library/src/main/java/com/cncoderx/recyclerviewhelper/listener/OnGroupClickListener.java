package com.cncoderx.recyclerviewhelper.listener;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * @author cncoderx
 */
public interface OnGroupClickListener {
    void onGroupClick(RecyclerView parent, View v, int groupPosition,
                         long id);
}
