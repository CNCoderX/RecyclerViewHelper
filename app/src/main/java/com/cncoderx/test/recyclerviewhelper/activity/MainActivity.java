package com.cncoderx.test.recyclerviewhelper.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new String[] {
                        "Item Click/LongClick",
                        "Header/Footer View",
                        "Load More Data",
                        "Expandable Item",
                        "Swipe Layout",
                        "Data Filter"
                }));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, ItemClickEventActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, HeaderOrFooterActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, LoadMoreActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, ExpandableItemActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, SwipeLayoutActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, DataFilterActivity.class));
                break;
            default:
                break;
        }
    }
}
