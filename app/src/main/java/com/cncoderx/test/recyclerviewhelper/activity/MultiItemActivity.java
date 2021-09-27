package com.cncoderx.test.recyclerviewhelper.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.test.recyclerviewhelper.adapter.MultiAlbumAdapter;
import com.cncoderx.test.recyclerviewhelper.data.AlbumManager;

public class MultiItemActivity extends RecyclerViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView.Adapter adapter = new MultiAlbumAdapter(AlbumManager.obtainAlbumList());
        RecyclerViewHelper.setAdapter(mRecyclerView, adapter);
    }
}
