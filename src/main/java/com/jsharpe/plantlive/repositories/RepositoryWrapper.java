package com.jsharpe.plantlive.repositories;

import com.jsharpe.plantlive.repositories.in.InRepository;
import com.jsharpe.plantlive.repositories.out.OutRepository;

public class RepositoryWrapper {

    private final InRepository inRepository;
    private final OutRepository outRepository;

    public RepositoryWrapper(InRepository inRepository, OutRepository outRepository) {
        this.inRepository = inRepository;
        this.outRepository = outRepository;
    }

    public InRepository getInRepository() {
        return inRepository;
    }

    public OutRepository getOutRepository() {
        return outRepository;
    }

}
