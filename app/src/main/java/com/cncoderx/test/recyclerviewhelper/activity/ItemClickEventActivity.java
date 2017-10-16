package com.cncoderx.test.recyclerviewhelper.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnItemLongClickListener;
import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.adapter.AlbumAdapter;
import com.cncoderx.test.recyclerviewhelper.data.AlbumManager;
import com.cncoderx.test.recyclerviewhelper.utils.Layout;

/**
 * @author cncoderx
 */
public class ItemClickEventActivity extends RecyclerViewActivity {

    @Override
    protected void onLayoutManagerChanged(Layout layout) {
        super.onLayoutManagerChanged(layout);

        RecyclerView.Adapter adapter = null;
        switch (layout) {
            case linear:
                adapter = new AlbumAdapter(R.layout.item_album_linear_layout, AlbumManager.obtainAlbumList());
                break;
            case grid:
                adapter = new AlbumAdapter(R.layout.item_album_grid_layout, AlbumManager.obtainAlbumList());
                break;
            case staggered:
                adapter = new AlbumAdapter(R.layout.item_album_staggered_layout, AlbumManager.obtainAlbumList());
                break;
        }

        RecyclerViewHelper.setAdapter(mRecyclerView, adapter);
        RecyclerViewHelper.setOnItemClickListener(mRecyclerView, new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "click item, position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerViewHelper.setOnItemLongClickListener(mRecyclerView, new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "long click item, position:" + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
