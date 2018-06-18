package com.github.codetanzania.feature.list;


import android.support.v7.widget.RecyclerView;

public abstract class ClickAwareRecyclerViewAdapter<T> extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final OnItemClickListener<T> mClickListener;

    public ClickAwareRecyclerViewAdapter(OnItemClickListener<T> mClickListener) {
        this.mClickListener = mClickListener;
    }
}
