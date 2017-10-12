package com.cncoderx.recyclerviewhelper.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author cncoderx
 */
public interface OnItemLongClickListener {
    boolean onItemLongClick(RecyclerView parent, View view, int position, long id);
}
