package com.cncoderx.test.recyclerviewhelper.activity;

import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.adapter.AlbumSwipeAdapter;
import com.cncoderx.test.recyclerviewhelper.data.AlbumManager;
import com.cncoderx.test.recyclerviewhelper.utils.Layout;
import com.daimajia.swipe.util.Attributes;

/**
 * @author cncoderx
 */
public class SwipeLayoutActivity extends RecyclerViewActivity {

    @Override
    protected void onLayoutManagerChanged(Layout layout) {
        super.onLayoutManagerChanged(layout);
        AlbumSwipeAdapter adapter = null;
        switch (layout) {
            case linear:
                adapter = new AlbumSwipeAdapter(R.layout.item_album_linear_swipe_layout, AlbumManager.obtainAlbumList());
                break;
            case grid:
                adapter = new AlbumSwipeAdapter(R.layout.item_album_grid_swipe_layout, AlbumManager.obtainAlbumList());
                break;
            case staggered:
                adapter = new AlbumSwipeAdapter(R.layout.item_album_staggered_swipe_layout, AlbumManager.obtainAlbumList());
                break;
        }
        adapter.setMode(Attributes.Mode.Multiple);
        RecyclerViewHelper.setAdapter(mRecyclerView, adapter);
    }
}
