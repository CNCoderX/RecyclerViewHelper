package com.cncoderx.recyclerviewhelper.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author cncoderx
 */
public interface OnGroupLongClickListener {
    boolean onGroupLongClick(RecyclerView parent, View v, int groupPosition,
                         long id);
}
