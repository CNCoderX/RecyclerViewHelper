package com.cncoderx.test.recyclerviewhelper.adapter;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ExpandableAdapter;
import com.cncoderx.test.recyclerviewhelper.R;
import com.cncoderx.test.recyclerviewhelper.data.Album;

import java.util.List;

/**
 * @author cncoderx
 */
public class ExpandableAlbumAdapter extends ExpandableAdapter<ExpandableAlbumAdapter.GroupViewHolder, ExpandableAlbumAdapter.ChildViewHolder> {
    private List<String> mGroupData;
    private List<List<Album>> mChildData;
    private @LayoutRes int mGroupLayout, mChildLayout;

    public ExpandableAlbumAdapter(List<String> groupData,
                                  List<List<Album>> childData,
                                  @LayoutRes int groupLayout,
                                  @LayoutRes int childLayout) {
        mGroupData = groupData;
        mChildData = childData;
        mGroupLayout = groupLayout;
        mChildLayout = childLayout;
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mGroupLayout, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, int groupPosition) {
        holder.onBind(mGroupData.get(groupPosition), groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mGroupData.size();
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mChildLayout, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int groupPosition, int childPosition) {
        holder.onBind(mChildData.get(groupPosition).get(childPosition), groupPosition, childPosition);
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mChildData.get(groupPosition).size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public GroupViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }

        public void onBind(String text, int groupPosition) {
            mTextView.setText(text);
        }
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvName;
        TextView tvPrice;

        public ChildViewHolder(View itemView) {
            super(itemView);
            ivCover = (ImageView) itemView.findViewById(R.id.cover);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvPrice = (TextView) itemView.findViewById(R.id.price);
        }

        public void onBind(Album album, int groupPosition, int childPosition) {
            ivCover.setImageResource(album.getCover());
            tvName.setText(album.getName());
            tvPrice.setText(album.getPrice());
        }
    }
}
