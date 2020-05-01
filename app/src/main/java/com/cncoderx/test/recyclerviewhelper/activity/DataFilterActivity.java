package com.cncoderx.test.recyclerviewhelper.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.adapter.AlbumAdapter;
import com.cncoderx.test.recyclerviewhelper.data.AlbumManager;
import com.cncoderx.test.recyclerviewhelper.utils.Layout;

public class DataFilterActivity extends RecyclerViewActivity {
    private String mSearchKey;

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
        if (!TextUtils.isEmpty(mSearchKey)) {
            RecyclerViewHelper.setFilterText(mRecyclerView, mSearchKey);
        }
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
                            mSearchKey = editText.getText().toString();
                            RecyclerViewHelper.setFilterText(mRecyclerView, mSearchKey);
                        }
                    }).show();
        }
        return true;
    }
}
