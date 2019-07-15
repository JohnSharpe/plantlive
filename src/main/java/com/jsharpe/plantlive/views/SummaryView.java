package com.jsharpe.plantlive.views;

import com.jsharpe.plantlive.api.Summary;
import io.dropwizard.views.View;

public class SummaryView extends View {

    private final Summary summary;

    public SummaryView(final Summary summary) {
        super("summary.ftl");
        this.summary = summary;
    }

    public Summary getSummary() {
        return summary;
    }
}
