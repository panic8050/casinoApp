package com.mapplic.slots.logic;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

public abstract class Callback {
    public abstract void OnFinishListener();

    private List<LinearLayoutManager> lms;

    public void setLayoutManagers(List<LinearLayoutManager> layoutManagers) {
        this.lms = layoutManagers;
    }

    public List<LinearLayoutManager> getLayoutManagers() {
        return lms;
    }
}
