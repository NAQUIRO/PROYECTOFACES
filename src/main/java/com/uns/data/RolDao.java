package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Rol;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class RolDao extends JPA implements DAO<Rol> {

    private static final Logger logger = LoggerFactory.getLogger(RolDao.class);

    @Override
    public Rol getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Rol.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar rol: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar rol", e);
            }
        });
    }

    @Override
    public List<Rol> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery("SELECT r FROM Rol r ORDER BY r.id DESC", Rol.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener roles: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener roles", e);
            }
        });
    }

    @Override
    public Rol create(Rol entity) {
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
            logger.error("Error al crear rol: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear rol", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Rol update(Rol entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Rol updateObj = em.find(Rol.class, entity.getId());
            updateObj.setNombre(entity.getNombre());
            updateObj.setDescripcion(entity.getDescripcion());

            t.begin();
            updateObj = em.merge(updateObj);
            t.commit();
            return updateObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al actualizar rol: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar rol", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Rol delete(Rol entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Rol deleteObj = em.find(Rol.class, entity.getId());

            t.begin();
            em.remove(deleteObj);
            t.commit();
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar rol: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar rol", e);
        } finally {
            closeEm(em);
        }
    }
}