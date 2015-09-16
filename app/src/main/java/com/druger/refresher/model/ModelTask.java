package com.druger.refresher.model;

/**
 * Created by druger on 16.09.2015.
 */
public class ModelTask implements Item {

    private String title;
    private long date;

    public ModelTask() {
    }

    public ModelTask(String title, long date) {
        this.title = title;
        this.date = date;
    }

    @Override
    public boolean isTask() {
        return true;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
