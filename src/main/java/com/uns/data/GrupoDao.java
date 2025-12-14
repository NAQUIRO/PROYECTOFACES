package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Grupo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class GrupoDao extends JPA implements DAO<Grupo> {

    private static final Logger logger = LoggerFactory.getLogger(GrupoDao.class);

    @Override
    public Grupo getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Grupo.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar grupo: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar grupo", e);
            }
        });
    }

    @Override
    public List<Grupo> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery("SELECT g FROM Grupo g ORDER BY g.id DESC", Grupo.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener grupos: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener grupos", e);
            }
        });
    }

    public List<Grupo> getAllActivos() {
        return executeQueryList(em -> {
            try {
                return em.createQuery("SELECT g FROM Grupo g WHERE g.estado = 'Activo' ORDER BY g.nombre", Grupo.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener grupos activos: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener grupos activos", e);
            }
        });
    }

    @Override
    public Grupo create(Grupo entity) {
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
            logger.error("Error al crear grupo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear grupo", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Grupo update(Grupo entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Grupo updateObj = em.find(Grupo.class, entity.getId());
            updateObj.setCodgrupo(entity.getCodgrupo());
            updateObj.setNombre(entity.getNombre());

            t.begin();
            updateObj = em.merge(updateObj);
            t.commit();
            return updateObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al actualizar grupo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar grupo", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Grupo delete(Grupo entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Grupo deleteObj = em.find(Grupo.class, entity.getId());
            deleteObj.setEstado(Grupo.Estado.Baja);

            t.begin();
            deleteObj = em.merge(deleteObj);
            t.commit();
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar grupo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar grupo", e);
        } finally {
            closeEm(em);
        }
    }
}