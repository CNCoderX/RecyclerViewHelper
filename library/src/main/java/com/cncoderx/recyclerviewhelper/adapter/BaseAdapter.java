package com.cncoderx.recyclerviewhelper.adapter;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author cncoderx
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = onCreateView(inflater, parent, viewType);
        return new BaseViewHolder(itemView);
    }

    protected abstract View onCreateView(LayoutInflater inflater, ViewGroup parent, int viewType);

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mIDArray = new SparseArray<>();

        public Object object;

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public View getView(@IdRes int id) {
            return getView(id, View.class);
        }

        public <T extends View> T getView(@IdRes int id, Class<T> clazz) {
            View v = mIDArray.get(id);
            if (v == null) {
                v = itemView.findViewById(id);
                mIDArray.put(id, v);
            }
            //noinspection unchecked
            return (T) v;
        }
    }
}
