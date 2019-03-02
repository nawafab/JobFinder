package com.nawaf.jobfinder.models;

import java.io.Serializable;

public class FilterModel implements Serializable {

    private String title;
    private String location;
    private Providers providers = Providers.GITHUB;

    public String getTitle() {
        return title != null ? title : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {

        return location != null ? location: "";

    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Providers getProviders() {
        return providers != null ? providers: Providers.GITHUB;
    }

    public void setProviders(Providers providers) {
        this.providers = providers;
    }


}
