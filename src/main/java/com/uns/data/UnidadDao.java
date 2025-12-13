package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Unidad;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class UnidadDao extends JPA implements DAO<Unidad> {

    private static final Logger logger = LoggerFactory.getLogger(UnidadDao.class);

    @Override
    public Unidad getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Unidad.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar unidad: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar unidad", e);
            }
        });
    }

    @Override
    public List<Unidad> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery("SELECT u FROM Unidad u ORDER BY u.id DESC", Unidad.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener unidades: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener unidades", e);
            }
        });
    }

    public List<Unidad> getAllActivos() {
        return executeQueryList(em -> {
            try {
                // CORREGIDO: Ordenar por abreviatura en lugar de nombre (que no existe)
                return em.createQuery("SELECT u FROM Unidad u WHERE u.estado = 'Activo' ORDER BY u.abreviatura", Unidad.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener unidades activas: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener unidades activas", e);
            }
        });
    }

    @Override
    public Unidad create(Unidad entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            t.begin();
            em.persist(entity);
            t.commit();
            return entity;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al crear unidad: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear unidad", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Unidad update(Unidad entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Unidad updateObj = em.find(Unidad.class, entity.getId());
            updateObj.setAbreviatura(entity.getAbreviatura());
            updateObj.setDescripcion(entity.getDescripcion());
            updateObj.setEstado(entity.getEstado());

            t.begin();
            updateObj = em.merge(updateObj);
            t.commit();
            return updateObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al actualizar unidad: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar unidad", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Unidad delete(Unidad entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Unidad deleteObj = em.find(Unidad.class, entity.getId());
            deleteObj.setEstado("Baja");

            t.begin();
            deleteObj = em.merge(deleteObj);
            t.commit();
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar unidad: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar unidad", e);
        } finally {
            closeEm(em);
        }
    }
}