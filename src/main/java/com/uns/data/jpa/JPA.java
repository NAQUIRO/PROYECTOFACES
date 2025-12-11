package com.uns.data.jpa;

import jakarta.persistence.EntityManager;

public class JPA extends JPAFactory {

    public EntityManager getEm() {
        return JPAFactory.createEntityManager();
    }

    public void closeEntityManager() {
        JPAFactory.close();
    }
}
