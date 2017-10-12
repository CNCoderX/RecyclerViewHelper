package com.cncoderx.test.recyclerviewhelper.activity;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

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
 * Created by admin on 2017/10/9.
 */
public class LoadMoreActivity extends RecyclerViewActivity {

    @Override
    protected void onLayoutManagerChanged(Layout layout) {
        super.onLayoutManagerChanged(layout);
        RecyclerView.Adapter adapter = null;
        switch (layout) {
            case linear:
                adapter = new AlbumAdapter(R.layout.item_album_linear_layout, AlbumManager.obtainAlbumList(0, 10));
                break;
            case grid:
                adapter = new AlbumAdapter(R.layout.item_album_grid_layout, AlbumManager.obtainAlbumList(0, 10));
                break;
            case staggered:
                adapter = new AlbumAdapter(R.layout.item_album_staggered_layout, AlbumManager.obtainAlbumList(0, 10));
                break;
        }

        RecyclerViewHelper.setAdapter(mRecyclerView, adapter);
        RecyclerViewHelper.setLoadMoreListener(mRecyclerView, new OnLoadMoreListener() {
            @Override
            public void load(RecyclerView recyclerView, ILoadingView view) {
                new LoadingTask((AlbumAdapter) RecyclerViewHelper.getAdapter(recyclerView), view).execute();
            }
        });
    }

    static class LoadingTask extends AsyncTask<Integer, Void, List<Album>> {
        AlbumAdapter adapter;
        ILoadingView view;

        public LoadingTask(AlbumAdapter adapter, ILoadingView view) {
            this.adapter = adapter;
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            view.show();
        }

        @Override
        protected List<Album> doInBackground(Integer... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return AlbumManager.obtainAlbumList(adapter.size(), 4);
        }

        @Override
        protected void onPostExecute(List<Album> albums) {
            final int count = albums.size();
            if (count == 0) {
                view.end();
            } else {
                adapter.addAll(albums);
                view.hidden();
            }
        }
    }
}
