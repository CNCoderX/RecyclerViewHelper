package com.cncoderx.test.recyclerviewhelper.activity;

import android.view.LayoutInflater;
import android.view.View;

import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.test.recyclerviewhelper.utils.Layout;
import com.cncoderx.test.recyclerviewhelper.R;

public class HeaderOrFooterActivity extends ItemClickEventActivity {

    @Override
    protected void onLayoutManagerChanged(Layout layout) {
        super.onLayoutManagerChanged(layout);

        // new header view
        View headerView = LayoutInflater.from(this).inflate(R.layout.item_header_layout, mRecyclerView, false);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewHelper.removeHeaderView(mRecyclerView, v);
            }
        });
        RecyclerViewHelper.addHeaderView(mRecyclerView, headerView);

        // new footer view
        View footerView = LayoutInflater.from(this).inflate(R.layout.item_footer_layout, mRecyclerView, false);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewHelper.removeFooterView(mRecyclerView, v);
            }
        });
        RecyclerViewHelper.addFooterView(mRecyclerView, footerView);
    }
}
