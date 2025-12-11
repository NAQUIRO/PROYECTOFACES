package com.uns.data;

import ch.qos.logback.classic.Logger;
import com.uns.data.jpa.JPA;
import com.uns.entities.Unidad;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class UnidadDao extends JPA implements DAO<Unidad> {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(Unidad.class);

    @Override
    public Unidad getById(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("ID inválido");
            }

            Unidad unidad = getEm().find(Unidad.class, id);
            if (unidad == null) {
                throw new EntityNotFoundException("No se encontró la unidad con ID: " + id);
            }

            return unidad;

        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw new RuntimeException("Error al buscar la unidad: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Unidad> getAll() {
        try {
            return getEm().createQuery("SELECT u FROM Unidad u ORDER BY u.id  DESC", Unidad.class)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las unidades: " + e.getMessage(), e);
        }
    }

    @Override
    public Unidad create(Unidad entity) {
        EntityTransaction t = getEm().getTransaction();
        try {
            t.begin();
            getEm().persist(entity);
            t.commit();
            return entity;

        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }

            logger.error("Error al persistir unidad: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar la unidad", e);
        }

    }

    @Override
    public Unidad update(Unidad entity) {
        EntityTransaction t = getEm().getTransaction();
        try {

            Unidad updateObj = getEm().find(Unidad.class, entity.getId());

            updateObj.setAbreviatura(entity.getAbreviatura());
            updateObj.setDescripcion(entity.getDescripcion());
            t.begin();
            updateObj = getEm().merge(updateObj);
            t.commit();
            return updateObj;

        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }

            logger.error("Error al persistir unidad: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar la unidad", e);
        }

    }

    @Override
    public Unidad delete(Unidad entity) {
        EntityTransaction t = getEm().getTransaction();
        try {
           
            System.out.println(entity);
            Unidad deleteObj = getEm().find(Unidad.class, entity.getId());
            deleteObj.setEstado("Baja");

            t.begin();
            deleteObj = getEm().merge(deleteObj);
            t.commit();
            return deleteObj;

        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }

            logger.error("Error al persistir unidad: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar la unidad", e);
        }

    }


}

