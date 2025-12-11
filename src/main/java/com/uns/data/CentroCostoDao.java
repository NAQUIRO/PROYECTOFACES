package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Centro_costo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class CentroCostoDao extends JPA implements DAO<Centro_costo> {

    private static final Logger logger = LoggerFactory.getLogger(CentroCostoDao.class);

    @Override
    public Centro_costo getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Centro_costo.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar centro de costo: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar centro de costo", e);
            }
        });
    }

    @Override
    public List<Centro_costo> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery("SELECT c FROM Centro_costo c ORDER BY c.id DESC", Centro_costo.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener centros de costo: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener centros de costo", e);
            }
        });
    }

    public List<Centro_costo> getAllActivos() {
        return executeQueryList(em -> {
            try {
                return em
                        .createQuery("SELECT c FROM Centro_costo c WHERE c.estado = 'Activo' ORDER BY c.nombre",
                                Centro_costo.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener centros de costo activos: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener centros de costo activos", e);
            }
        });
    }

    @Override
    public Centro_costo create(Centro_costo entity) {
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
            logger.error("Error al crear centro de costo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear centro de costo", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Centro_costo update(Centro_costo entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Centro_costo updateObj = em.find(Centro_costo.class, entity.getId());
            updateObj.setCodigo(entity.getCodigo());
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
            logger.error("Error al actualizar centro de costo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar centro de costo", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Centro_costo delete(Centro_costo entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Centro_costo deleteObj = em.find(Centro_costo.class, entity.getId());
            deleteObj.setEstado(Centro_costo.Estado.Inactivo);

            t.begin();
            deleteObj = em.merge(deleteObj);
            t.commit();
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar centro de costo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar centro de costo", e);
        } finally {
            closeEm(em);
        }
    }
}