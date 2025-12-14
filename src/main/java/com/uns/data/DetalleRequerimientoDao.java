package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Detalle_requerimiento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class DetalleRequerimientoDao extends JPA implements DAO<Detalle_requerimiento> {

    private static final Logger logger = LoggerFactory.getLogger(DetalleRequerimientoDao.class);

    @Override
    public Detalle_requerimiento getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Detalle_requerimiento.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar detalle de requerimiento: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar detalle de requerimiento", e);
            }
        });
    }

    @Override
    public List<Detalle_requerimiento> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT d FROM Detalle_requerimiento d ORDER BY d.id DESC",
                        Detalle_requerimiento.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener detalles de requerimientos: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener detalles de requerimientos", e);
            }
        });
    }

    public List<Detalle_requerimiento> getByRequerimiento(Long requerimientoId) {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT d FROM Detalle_requerimiento d WHERE d.requerimiento.id = :requerimientoId ORDER BY d.id",
                        Detalle_requerimiento.class)
                        .setParameter("requerimientoId", requerimientoId)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener detalles de requerimiento {}: {}", requerimientoId, e.getMessage(), e);
                throw new RuntimeException("Error al obtener detalles de requerimiento", e);
            }
        });
    }

    public List<Detalle_requerimiento> getByEstado(Detalle_requerimiento.Estado estado) {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT d FROM Detalle_requerimiento d WHERE d.estado = :estado ORDER BY d.id DESC",
                        Detalle_requerimiento.class)
                        .setParameter("estado", estado)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener detalles de requerimientos por estado {}: {}", estado, e.getMessage(),
                        e);
                throw new RuntimeException("Error al obtener detalles de requerimientos por estado", e);
            }
        });
    }

    public List<Detalle_requerimiento> getPendientesByRequerimiento(Long requerimientoId) {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT d FROM Detalle_requerimiento d WHERE d.requerimiento.id = :requerimientoId AND d.estado = 'Pendiente' ORDER BY d.id",
                        Detalle_requerimiento.class)
                        .setParameter("requerimientoId", requerimientoId)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener detalles pendientes de requerimiento {}: {}", requerimientoId,
                        e.getMessage(), e);
                throw new RuntimeException("Error al obtener detalles pendientes de requerimiento", e);
            }
        });
    }

    @Override
    public Detalle_requerimiento create(Detalle_requerimiento entity) {
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
            logger.error("Error al crear detalle de requerimiento: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear detalle de requerimiento", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Detalle_requerimiento update(Detalle_requerimiento entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Detalle_requerimiento updateObj = em.find(Detalle_requerimiento.class, entity.getId());

            updateObj.setRequerimiento(entity.getRequerimiento());
            updateObj.setMaterial(entity.getMaterial());
            updateObj.setCantidad(entity.getCantidad());
            updateObj.setUnidad(entity.getUnidad());
            updateObj.setObservacion(entity.getObservacion());
            updateObj.setEstado(entity.getEstado());

            t.begin();
            updateObj = em.merge(updateObj);
            t.commit();
            return updateObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al actualizar detalle de requerimiento: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar detalle de requerimiento", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Detalle_requerimiento delete(Detalle_requerimiento entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Detalle_requerimiento deleteObj = em.find(Detalle_requerimiento.class, entity.getId());

            t.begin();
            em.remove(deleteObj);
            t.commit();
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar detalle de requerimiento: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar detalle de requerimiento", e);
        } finally {
            closeEm(em);
        }
    }

    public void deleteByRequerimiento(Long requerimientoId) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            t.begin();
            em.createQuery("DELETE FROM Detalle_requerimiento d WHERE d.requerimiento.id = :requerimientoId")
                    .setParameter("requerimientoId", requerimientoId)
                    .executeUpdate();
            t.commit();
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar detalles de requerimiento {}: {}", requerimientoId, e.getMessage(), e);
            throw new RuntimeException("Error al eliminar detalles de requerimiento", e);
        } finally {
            closeEm(em);
        }
    }
}
