package com.zapps.slots.logic;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

public abstract class Callback {
    public abstract void OnFinishListener();

    private List<LinearLayoutManager> layoutManagers;

    public void setLayoutManagers(List<LinearLayoutManager> layoutManagers) {
        this.layoutManagers = layoutManagers;
    }

    public List<LinearLayoutManager> getLayoutManagers() {
        return layoutManagers;
    }
}