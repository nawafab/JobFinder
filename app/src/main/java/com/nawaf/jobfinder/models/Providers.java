package com.nawaf.jobfinder.models;

import java.io.Serializable;

/**
 * enum represent the providers for the search job apis added in the app
 * you need to add the new provider with id and name for the new api added.
 */
public enum Providers implements Serializable {

    GITHUB(1,"GitHub"),
    SEARCH_GOV(2,"Search.gov");

    int id;
    String name;

    Providers(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
