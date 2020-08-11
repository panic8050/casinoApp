package com.zapps.slots.logic;

import androidx.recyclerview.widget.RecyclerView;

public class ScrollingListener extends RecyclerView.OnScrollListener {
    private Callback callback;

    public ScrollingListener(Callback callback) {
        this.callback = callback;
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                callback.OnFinishListener();
        }

    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }
}