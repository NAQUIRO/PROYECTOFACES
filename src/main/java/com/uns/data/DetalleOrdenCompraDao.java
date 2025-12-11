package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Detalle_orden_compra;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class DetalleOrdenCompraDao extends JPA implements DAO<Detalle_orden_compra> {

    private static final Logger logger = LoggerFactory.getLogger(DetalleOrdenCompraDao.class);

    @Override
    public Detalle_orden_compra getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Detalle_orden_compra.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar detalle de orden de compra: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar detalle de orden de compra", e);
            }
        });
    }

    @Override
    public List<Detalle_orden_compra> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT d FROM Detalle_orden_compra d " +
                                "LEFT JOIN FETCH d.ordenCompra " +
                                "LEFT JOIN FETCH d.material " +
                                "LEFT JOIN FETCH d.unidad " +
                                "ORDER BY d.id DESC",
                        Detalle_orden_compra.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener detalles de órdenes de compra: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener detalles de órdenes de compra", e);
            }
        });
    }

    /**
     * Obtiene todos los detalles de una orden de compra específica.
     * 
     * @param ordenCompraId ID de la orden de compra
     * @return Lista de detalles de la orden
     */
    public List<Detalle_orden_compra> getByOrdenCompra(Long ordenCompraId) {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT d FROM Detalle_orden_compra d " +
                                "LEFT JOIN FETCH d.material " +
                                "LEFT JOIN FETCH d.unidad " +
                                "WHERE d.ordenCompra.id = :ordenCompraId",
                        Detalle_orden_compra.class)
                        .setParameter("ordenCompraId", ordenCompraId)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener detalles de orden de compra {}: {}", ordenCompraId,
                        e.getMessage(), e);
                throw new RuntimeException("Error al obtener detalles de orden de compra", e);
            }
        });
    }

    @Override
    public Detalle_orden_compra create(Detalle_orden_compra entity) {
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
            logger.error("Error al crear detalle de orden de compra: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear detalle de orden de compra", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Detalle_orden_compra update(Detalle_orden_compra entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Detalle_orden_compra updateObj = em.find(Detalle_orden_compra.class, entity.getId());

            updateObj.setOrdenCompra(entity.getOrdenCompra());
            updateObj.setDetalleRequerimiento(entity.getDetalleRequerimiento());
            updateObj.setMaterial(entity.getMaterial());
            updateObj.setUnidad(entity.getUnidad());
            updateObj.setCantidad(entity.getCantidad());
            updateObj.setPrecioUnitario(entity.getPrecioUnitario());
            updateObj.setSubtotal(entity.getSubtotal());

            t.begin();
            updateObj = em.merge(updateObj);
            t.commit();
            return updateObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al actualizar detalle de orden de compra: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar detalle de orden de compra", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Detalle_orden_compra delete(Detalle_orden_compra entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Detalle_orden_compra deleteObj = em.find(Detalle_orden_compra.class, entity.getId());

            t.begin();
            em.remove(deleteObj);
            t.commit();
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar detalle de orden de compra: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar detalle de orden de compra", e);
        } finally {
            closeEm(em);
        }
    }

    /**
     * Elimina todos los detalles de una orden de compra específica.
     * 
     * @param ordenCompraId ID de la orden de compra
     */
    public void deleteByOrdenCompra(Long ordenCompraId) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            t.begin();
            em.createQuery(
                    "DELETE FROM Detalle_orden_compra d WHERE d.ordenCompra.id = :ordenCompraId")
                    .setParameter("ordenCompraId", ordenCompraId)
                    .executeUpdate();
            t.commit();
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar detalles de orden de compra {}: {}", ordenCompraId,
                    e.getMessage(), e);
            throw new RuntimeException("Error al eliminar detalles de orden de compra", e);
        } finally {
            closeEm(em);
        }
    }
}
