package com.cncoderx.test.recyclerviewhelper.activity;

import android.os.AsyncTask;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;
import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.adapter.AlbumAdapter;
import com.cncoderx.test.recyclerviewhelper.data.Album;
import com.cncoderx.test.recyclerviewhelper.data.AlbumManager;
import com.cncoderx.test.recyclerviewhelper.utils.Layout;

import java.util.List;

/**
 * @author cncoderx
 */
public class LoadMoreActivity extends RecyclerViewActivity {
    AlbumAdapter mAdapter;

    @Override
    protected void onLayoutManagerChanged(Layout layout) {
        super.onLayoutManagerChanged(layout);
        switch (layout) {
            case linear:
                mAdapter = new AlbumAdapter(R.layout.item_album_linear_layout, AlbumManager.obtainAlbumList(0, 10));
                break;
            case grid:
                mAdapter = new AlbumAdapter(R.layout.item_album_grid_layout, AlbumManager.obtainAlbumList(0, 10));
                break;
            case staggered:
                mAdapter = new AlbumAdapter(R.layout.item_album_staggered_layout, AlbumManager.obtainAlbumList(0, 10));
                break;
        }

        RecyclerViewHelper.setAdapter(mRecyclerView, mAdapter);
        RecyclerViewHelper.setLoadMoreListener(mRecyclerView, new OnLoadMoreListener() {
            @Override
            public void load(RecyclerView recyclerView, ILoadingView view) {
                new LoadingTask(mAdapter, null, view).execute();
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mAdapter.isEmpty()) {
                    mAdapter.clear();
                }
                new LoadingTask(mAdapter, mRefreshLayout, null).execute();
            }
        });
    }

    @Override
    protected boolean isRefreshable() {
        return true;
    }

    static class LoadingTask extends AsyncTask<Integer, Void, List<Album>> {
        AlbumAdapter adapter;
        ILoadingView view;
        SwipeRefreshLayout refresh;

        public LoadingTask(AlbumAdapter adapter,
                           @Nullable SwipeRefreshLayout refresh,
                           @Nullable ILoadingView view) {
            this.adapter = adapter;
            this.refresh = refresh;
            this.view = view;
        }

        @Override
        protected List<Album> doInBackground(Integer... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return AlbumManager.obtainAlbumList(adapter.size(), refresh == null ? 4 : 10);
        }

        @Override
        protected void onPostExecute(List<Album> albums) {
            if (refresh != null)
                refresh.setRefreshing(false);

            final int count = albums.size();
            if (count == 0) {
                if (view != null)
                    view.gone();
            } else {
                adapter.addAll(albums);
                if (view != null)
                    view.hidden();
            }
        }
    }
}
