package com.ekumfi.juice.pojo;

/**
 * Created by Andy on 2/25/2020.
 */

public class FAQ {
    String title, description;

    public FAQ(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
