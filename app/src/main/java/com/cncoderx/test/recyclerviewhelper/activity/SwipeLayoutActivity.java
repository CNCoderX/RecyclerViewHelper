package com.cncoderx.test.recyclerviewhelper.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(true);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        super.onMenuItemSelected(featureId, item);
        if (item.getItemId() == R.id.action_search) {
            final EditText editText = new EditText(this);
            editText.setSingleLine();
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(android.R.string.search_go)
                    .setView(editText)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.search_go, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String mSearchKey = editText.getText().toString();
                            RecyclerViewHelper.setFilterText(mRecyclerView, mSearchKey);
                        }
                    }).show();
        }
        return true;
    }
}
