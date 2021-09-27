package com.cncoderx.test.recyclerviewhelper.activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cncoderx.recyclerviewhelper.adapter.ExpandableAdapter;
import com.cncoderx.recyclerviewhelper.listener.OnChildClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnChildLongClickListener;
import com.cncoderx.test.recyclerviewhelper.utils.Layout;
import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.adapter.ExpandableAlbumAdapter;
import com.cncoderx.test.recyclerviewhelper.data.Album;
import com.cncoderx.test.recyclerviewhelper.data.AlbumManager;

import java.util.ArrayList;
import java.util.List;

public class ExpandableItemActivity extends RecyclerViewActivity {

    @Override
    protected void onLayoutManagerChanged(Layout layout) {
        super.onLayoutManagerChanged(layout);

        ArrayList<String> group = new ArrayList<>();
        group.add("Classical");
        group.add("Country");
        group.add("Jazz");
        group.add("Pop");
        group.add("Rock");

        ArrayList<List<Album>> child = new ArrayList<>();
        child.add(AlbumManager.obtainAlbumList("classical"));
        child.add(AlbumManager.obtainAlbumList("country"));
        child.add(AlbumManager.obtainAlbumList("jazz"));
        child.add(AlbumManager.obtainAlbumList("pop"));
        child.add(AlbumManager.obtainAlbumList("rock"));

        ExpandableAdapter adapter = null;
        switch (layout) {
            case linear:
                adapter = new ExpandableAlbumAdapter(group, child,
                        R.layout.item_expandable_group, R.layout.item_album_linear_layout);
                break;
            case grid:
                adapter = new ExpandableAlbumAdapter(group, child,
                        R.layout.item_expandable_group, R.layout.item_album_grid_layout);
                break;
            case staggered:
                adapter = new ExpandableAlbumAdapter(group, child,
                        R.layout.item_expandable_group, R.layout.item_album_staggered_layout);
                break;
        }
        mRecyclerView.setAdapter(adapter);
        adapter.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public void onChildClick(RecyclerView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), "click child item, position:" + childPosition, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnChildLongClickListener(new OnChildLongClickListener() {
            @Override
            public boolean onChildLongClick(RecyclerView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), "long click child item, position:" + childPosition, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
