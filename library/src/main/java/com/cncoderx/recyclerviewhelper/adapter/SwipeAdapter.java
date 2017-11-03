package com.cncoderx.recyclerviewhelper.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.cncoderx.recyclerviewhelper.R;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author cncoderx
 */
public abstract class SwipeAdapter<T> extends ObjectAdapter<T> {
    private Attributes.Mode mode = Attributes.Mode.Single;
    private List<Boolean> openedPositions = new ArrayList<>();
    private SwipeLayout tempLayout;

    public SwipeAdapter(@LayoutRes int resource) {
        super(resource);
    }

    public SwipeAdapter(@LayoutRes int resource, @NonNull T[] objects) {
        super(resource, objects);
        openedPositions.addAll(obtainBoolCollection(objects.length));
    }

    public SwipeAdapter(@LayoutRes int resource, @NonNull Collection<? extends T> objects) {
        super(resource, objects);
        openedPositions.addAll(obtainBoolCollection(objects.size()));
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = super.onCreateViewHolder(parent, viewType);
        SwipeLayout swipeLayout;
        if (holder.itemView instanceof SwipeLayout) {
            swipeLayout = (SwipeLayout) holder.itemView;
        } else {
            swipeLayout = (SwipeLayout) holder.itemView.findViewById(R.id.swipe_layout);
        }
        if (swipeLayout == null) {
            throw new RuntimeException("Failed to find swipe layout with ID "
                    + holder.itemView.getResources().getResourceName(R.id.swipe_layout)
                    + " in item layout");
        }
        swipeLayout.addSwipeListener(new OnSwipeListener(holder));
        swipeLayout.addOnLayoutListener(new OnLayoutListener(holder));
        return holder;
    }

    private Collection<Boolean> obtainBoolCollection(int size) {
        ArrayList<Boolean> arrayList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(Boolean.FALSE);
        }
        return arrayList;
    }

    @Override
    public void add(@NonNull T object) {
        super.add(object);
        openedPositions.add(Boolean.FALSE);
    }

    @Override
    public void add(int index, @NonNull T object) {
        super.add(index, object);
        openedPositions.add(index, Boolean.FALSE);
    }

    @Override
    public void addAll(@NonNull Collection<? extends T> objects) {
        super.addAll(objects);
        openedPositions.addAll(obtainBoolCollection(objects.size()));
    }

    @Override
    public void addAll(int index, @NonNull Collection<? extends T> objects) {
        super.addAll(index, objects);
        openedPositions.addAll(index, obtainBoolCollection(objects.size()));
    }

    @Override
    public void remove(int index) {
        super.remove(index);
        openedPositions.remove(index);
    }

    @Override
    public void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        openedPositions.subList(fromIndex, toIndex).clear();
    }

    @Override
    public void swap(int index, int index2) {
        super.swap(index, index2);
        Boolean b = openedPositions.get(index);
        Boolean b2 = openedPositions.get(index2);
        openedPositions.set(index, b2);
        openedPositions.set(index2, b);
    }

    @Override
    public void sort(@NonNull Comparator<? super T> comparator) {
        super.sort(comparator);
        Collections.fill(openedPositions, Boolean.FALSE);
    }

    @Override
    public void clear() {
        super.clear();
        openedPositions.clear();
    }

    public Attributes.Mode getMode() {
        return mode;
    }

    public void setMode(Attributes.Mode mode) {
        this.mode = mode;
    }

    class OnLayoutListener implements SwipeLayout.OnLayout {
        WeakReference<RecyclerView.ViewHolder> viewHolderRef;

        OnLayoutListener(RecyclerView.ViewHolder holder) {
            viewHolderRef = new WeakReference<>(holder);
        }

        @Override
        public void onLayout(SwipeLayout layout) {
            RecyclerView.ViewHolder holder = viewHolderRef.get();
            if (holder != null) {
                int position = holder.getAdapterPosition();
                if (openedPositions.get(position)) {
                    layout.open(false, false);
                    tempLayout = layout;
                } else {
                    layout.close(false, false);
                }
            }
        }

    }

    class OnSwipeListener extends SimpleSwipeListener {
        WeakReference<RecyclerView.ViewHolder> viewHolderRef;

        OnSwipeListener(RecyclerView.ViewHolder holder) {
            viewHolderRef = new WeakReference<>(holder);
        }

        @Override
        public void onClose(SwipeLayout layout) {
            RecyclerView.ViewHolder holder = viewHolderRef.get();
            if (holder != null) {
                openedPositions.set(holder.getAdapterPosition(), Boolean.FALSE);
            }
            if (getMode() == Attributes.Mode.Single) {
                if (tempLayout == layout) {
                    tempLayout = null;
                }
            }
        }

        @Override
        public void onOpen(SwipeLayout layout) {
            if (getMode() == Attributes.Mode.Single) {
                Collections.fill(openedPositions, Boolean.FALSE);
                if (tempLayout != null && tempLayout != layout) {
                    tempLayout.close();
                }
                tempLayout = layout;
            }
            RecyclerView.ViewHolder holder = viewHolderRef.get();
            if (holder != null) {
                openedPositions.set(holder.getAdapterPosition(), Boolean.TRUE);
            }
        }
    }
}
