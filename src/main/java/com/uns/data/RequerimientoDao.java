package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Requerimiento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class RequerimientoDao extends JPA implements DAO<Requerimiento> {

    private static final Logger logger = LoggerFactory.getLogger(RequerimientoDao.class);

    @Override
    public Requerimiento getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Requerimiento.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar requerimiento: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar requerimiento", e);
            }
        });
    }

    @Override
    public List<Requerimiento> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT r FROM Requerimiento r " +
                                "LEFT JOIN FETCH r.solicitante " +
                                "LEFT JOIN FETCH r.proyecto " +
                                "LEFT JOIN FETCH r.area " +
                                "LEFT JOIN FETCH r.centroCosto " +
                                "ORDER BY r.id DESC",
                        Requerimiento.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener requerimientos: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener requerimientos", e);
            }
        });
    }

    public List<Requerimiento> getByEstado(Requerimiento.Estado estado) {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT r FROM Requerimiento r " +
                                "LEFT JOIN FETCH r.solicitante " +
                                "LEFT JOIN FETCH r.proyecto " +
                                "WHERE r.estado = :estado " +
                                "ORDER BY r.fechaSolicitud DESC",
                        Requerimiento.class)
                        .setParameter("estado", estado)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener requerimientos por estado: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener requerimientos por estado", e);
            }
        });
    }

    public List<Requerimiento> getBySolicitante(Long solicitanteId) {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT r FROM Requerimiento r " +
                                "WHERE r.solicitante.id = :solicitanteId " +
                                "ORDER BY r.fechaSolicitud DESC",
                        Requerimiento.class)
                        .setParameter("solicitanteId", solicitanteId)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener requerimientos por solicitante: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener requerimientos por solicitante", e);
            }
        });
    }

    @Override
    public Requerimiento create(Requerimiento entity) {
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
            logger.error("Error al crear requerimiento: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear requerimiento", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Requerimiento update(Requerimiento entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Requerimiento updateObj = em.find(Requerimiento.class, entity.getId());

            updateObj.setCodigo(entity.getCodigo());
            updateObj.setFechaSolicitud(entity.getFechaSolicitud());
            updateObj.setSolicitante(entity.getSolicitante());
            updateObj.setProyecto(entity.getProyecto());
            updateObj.setArea(entity.getArea());
            updateObj.setCentroCosto(entity.getCentroCosto());
            updateObj.setEtapa(entity.getEtapa());
            updateObj.setEstado(entity.getEstado());
            updateObj.setFechaAprobacion(entity.getFechaAprobacion());
            updateObj.setAprobador(entity.getAprobador());
            updateObj.setObservaciones(entity.getObservaciones());

            t.begin();
            updateObj = em.merge(updateObj);
            t.commit();
            return updateObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al actualizar requerimiento: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar requerimiento", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Requerimiento delete(Requerimiento entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Requerimiento deleteObj = em.find(Requerimiento.class, entity.getId());
            deleteObj.setEstado(Requerimiento.Estado.Cancelado);

            t.begin();
            deleteObj = em.merge(deleteObj);
            t.commit();
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar requerimiento: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar requerimiento", e);
        } finally {
            closeEm(em);
        }
    }
}