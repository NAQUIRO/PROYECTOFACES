package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Orden_compra;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class OrdenCompraDao extends JPA implements DAO<Orden_compra> {

    private static final Logger logger = LoggerFactory.getLogger(OrdenCompraDao.class);

    @Override
    public Orden_compra getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Orden_compra.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar orden de compra: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar orden de compra", e);
            }
        });
    }

    @Override
    public List<Orden_compra> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT o FROM Orden_compra o " +
                                "LEFT JOIN FETCH o.proveedor " +
                                "LEFT JOIN FETCH o.solicitante " +
                                "ORDER BY o.id DESC",
                        Orden_compra.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener órdenes de compra: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener órdenes de compra", e);
            }
        });
    }

    public List<Orden_compra> getByEstado(Orden_compra.Estado estado) {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT o FROM Orden_compra o " +
                                "LEFT JOIN FETCH o.proveedor " +
                                "LEFT JOIN FETCH o.solicitante " +
                                "WHERE o.estado = :estado " +
                                "ORDER BY o.fecha DESC",
                        Orden_compra.class)
                        .setParameter("estado", estado)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener órdenes por estado: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener órdenes por estado", e);
            }
        });
    }

    public List<Orden_compra> getByProveedor(Long proveedorId) {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT o FROM Orden_compra o " +
                                "WHERE o.proveedor.id = :proveedorId " +
                                "ORDER BY o.fecha DESC",
                        Orden_compra.class)
                        .setParameter("proveedorId", proveedorId)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener órdenes por proveedor: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener órdenes por proveedor", e);
            }
        });
    }

    @Override
    public Orden_compra create(Orden_compra entity) {
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
            logger.error("Error al crear orden de compra: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear orden de compra", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Orden_compra update(Orden_compra entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Orden_compra updateObj = em.find(Orden_compra.class, entity.getId());

            updateObj.setCodigo(entity.getCodigo());
            updateObj.setFecha(entity.getFecha());
            updateObj.setProveedor(entity.getProveedor());
            updateObj.setSolicitante(entity.getSolicitante());
            updateObj.setMoneda(entity.getMoneda());
            updateObj.setFormaPago(entity.getFormaPago());
            updateObj.setCondicionPago(entity.getCondicionPago());
            updateObj.setLugarEntrega(entity.getLugarEntrega());
            updateObj.setFechaEntrega(entity.getFechaEntrega());
            updateObj.setEstado(entity.getEstado());
            updateObj.setSubtotal(entity.getSubtotal());
            updateObj.setIgv(entity.getIgv());
            updateObj.setTotal(entity.getTotal());
            updateObj.setObservaciones(entity.getObservaciones());

            t.begin();
            updateObj = em.merge(updateObj);
            t.commit();
            return updateObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al actualizar orden de compra: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar orden de compra", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Orden_compra delete(Orden_compra entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Orden_compra deleteObj = em.find(Orden_compra.class, entity.getId());
            deleteObj.setEstado(Orden_compra.Estado.Anulada);

            t.begin();
            deleteObj = em.merge(deleteObj);
            t.commit();
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al anular orden de compra: {}", e.getMessage(), e);
            throw new RuntimeException("Error al anular orden de compra", e);
        } finally {
            closeEm(em);
        }
    }
}