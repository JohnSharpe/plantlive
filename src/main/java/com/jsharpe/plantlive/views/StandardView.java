package com.jsharpe.plantlive.views;

import io.dropwizard.views.View;

public class StandardView extends View {

    // We might be here because the plant specified does not exist.
    private final boolean plantNotFound;

    public StandardView(final boolean plantNotFound) {
        super("standard.ftl");
        this.plantNotFound = plantNotFound;
    }

    public boolean isPlantNotFound() {
        return plantNotFound;
    }
}
