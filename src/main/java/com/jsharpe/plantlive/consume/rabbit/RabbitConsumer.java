package com.jsharpe.plantlive.consume.rabbit;

import com.jsharpe.plantlive.consume.InService;
import io.dropwizard.lifecycle.Managed;

public class RabbitConsumer implements Managed {

    private final InService inService;

    public RabbitConsumer(InService inService) {
        this.inService = inService;
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {

    }

}
