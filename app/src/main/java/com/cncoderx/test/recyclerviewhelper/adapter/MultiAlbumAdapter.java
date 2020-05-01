package com.cncoderx.test.recyclerviewhelper.adapter;

import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.data.Album;

import java.util.Collection;

public class MultiAlbumAdapter extends AlbumAdapter {
    public static final int LINEAR_LAYOUT_VIEW_TYPE = 0;
    public static final int GRID_LAYOUT_VIEW_TYPE = 1;
    public static final int STAGGERED_LAYOUT_VIEW_TYPE = 2;

    public MultiAlbumAdapter() {
        super(0);
    }

    public MultiAlbumAdapter(Album... data) {
        super(0, data);
    }

    public MultiAlbumAdapter(Collection<? extends Album> data) {
        super(0, data);
    }

    @Override
    protected int getLayoutResource(int viewType) {
        int layoutRes = super.getLayoutResource(viewType);
        switch (viewType) {
            case LINEAR_LAYOUT_VIEW_TYPE:
                layoutRes = R.layout.item_album_linear_layout;
                break;
            case GRID_LAYOUT_VIEW_TYPE:
                layoutRes = R.layout.item_album_grid_layout;
                break;
            case STAGGERED_LAYOUT_VIEW_TYPE:
                layoutRes = R.layout.item_album_staggered_layout;
                break;
            default:
                break;
        }
        return layoutRes;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        switch (position % 3) {
            case 1:
                viewType = GRID_LAYOUT_VIEW_TYPE;
                break;
            case 2:
                viewType = STAGGERED_LAYOUT_VIEW_TYPE;
                break;
            case 0:
            default:
                viewType = LINEAR_LAYOUT_VIEW_TYPE;
                break;
        }
        return viewType;
    }
}
