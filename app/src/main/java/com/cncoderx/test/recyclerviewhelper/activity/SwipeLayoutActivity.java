package com.cncoderx.test.recyclerviewhelper.activity;

import android.support.v7.widget.RecyclerView;

import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.adapter.AlbumAdapter;
import com.cncoderx.test.recyclerviewhelper.data.AlbumManager;
import com.cncoderx.test.recyclerviewhelper.utils.Layout;

/**
 * @author cncoderx
 */
public class SwipeLayoutActivity extends RecyclerViewActivity {

    @Override
    protected void onLayoutManagerChanged(Layout layout) {
        super.onLayoutManagerChanged(layout);
        RecyclerView.Adapter adapter = null;
        switch (layout) {
            case linear:
                adapter = new AlbumAdapter(R.layout.item_album_linear_swipe_layout, AlbumManager.obtainAlbumList());
                break;
            case grid:
                adapter = new AlbumAdapter(R.layout.item_album_grid_swipe_layout, AlbumManager.obtainAlbumList());
                break;
            case staggered:
                adapter = new AlbumAdapter(R.layout.item_album_staggered_swipe_layout, AlbumManager.obtainAlbumList());
                break;
        }

        RecyclerViewHelper.setAdapter(mRecyclerView, adapter);
        RecyclerViewHelper.setSwipable(mRecyclerView, true);
    }
}
