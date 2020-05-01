package com.cncoderx.test.recyclerviewhelper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.cncoderx.test.recyclerviewhelper.utils.Layout;
import com.cncoderx.recyclerviewhelper.utils.DividerItemDecoration;
import com.cncoderx.recyclerviewhelper.utils.SpacingItemDecoration;
import com.cncoderx.test.recyclerviewhelper.R;

/**
 * @author cncoderx
 */
public class RecyclerViewActivity extends Activity {
    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    RecyclerView.ItemDecoration mItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        setLinearLayoutManager();
        mRefreshLayout.setEnabled(isRefreshable());
    }

    protected boolean isRefreshable() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_linear:
                setLinearLayoutManager();
                break;
            case R.id.action_grid:
                setGridLayoutManager();
                break;
            case R.id.action_staggered_grid:
                setStaggeredGridLayoutManager();
                break;
        }
        return true;
    }

    protected void onLayoutManagerChanged(Layout layout) {

    }

    private void setLinearLayoutManager() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mItemDecoration != null) mRecyclerView.removeItemDecoration(mItemDecoration);
        mItemDecoration = new DividerItemDecoration(getResources().getDrawable(R.drawable.divider),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, getResources().getDisplayMetrics()));
        mRecyclerView.addItemDecoration(mItemDecoration);
        onLayoutManagerChanged(Layout.linear);
    }

    private void setGridLayoutManager() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        if (mItemDecoration != null) mRecyclerView.removeItemDecoration(mItemDecoration);
        mItemDecoration = new SpacingItemDecoration(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, getResources().getDisplayMetrics()));
        mRecyclerView.addItemDecoration(mItemDecoration);
        onLayoutManagerChanged(Layout.grid);
    }

    private void setStaggeredGridLayoutManager() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        if (mItemDecoration != null) mRecyclerView.removeItemDecoration(mItemDecoration);
        mItemDecoration = new SpacingItemDecoration(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, getResources().getDisplayMetrics()));
        mRecyclerView.addItemDecoration(mItemDecoration);
        onLayoutManagerChanged(Layout.staggered);
    }
}
