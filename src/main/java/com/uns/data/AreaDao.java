package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Area;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class AreaDao extends JPA implements DAO<Area> {

    private static final Logger logger = LoggerFactory.getLogger(AreaDao.class);

    @Override
    public Area getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Area.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar área: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar área", e);
            }
        });
    }

    @Override
    public List<Area> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery("SELECT a FROM Area a ORDER BY a.id DESC", Area.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener áreas: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener áreas", e);
            }
        });
    }

    public List<Area> getAllActivos() {
        return executeQueryList(em -> {
            try {
                return em.createQuery("SELECT a FROM Area a WHERE a.estado = 'Activo' ORDER BY a.nombre", Area.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener áreas activas: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener áreas activas", e);
            }
        });
    }

    @Override
    public Area create(Area entity) {
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
            logger.error("Error al crear área: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear área", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Area update(Area entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Area updateObj = em.find(Area.class, entity.getId());
            updateObj.setCodigo(entity.getCodigo());
            updateObj.setNombre(entity.getNombre());
            updateObj.setCentroCosto(entity.getCentroCosto());
            updateObj.setDescripcion(entity.getDescripcion());

            t.begin();
            updateObj = em.merge(updateObj);
            t.commit();
            return updateObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al actualizar área: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar área", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Area delete(Area entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Area deleteObj = em.find(Area.class, entity.getId());
            deleteObj.setEstado(Area.Estado.Inactivo);

            t.begin();
            deleteObj = em.merge(deleteObj);
            t.commit();
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar área: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar área", e);
        } finally {
            closeEm(em);
        }
    }
}