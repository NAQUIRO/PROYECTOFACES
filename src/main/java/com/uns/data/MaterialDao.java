package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Material;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class MaterialDao extends JPA implements DAO<Material> {

    private static final Logger logger = LoggerFactory.getLogger(MaterialDao.class);

    @Override
    public Material getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Material.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar material: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar material", e);
            }
        });
    }

    @Override
    public List<Material> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT m FROM Material m " +
                                "LEFT JOIN FETCH m.grupo " +
                                "LEFT JOIN FETCH m.unidad " +
                                "ORDER BY m.id DESC",
                        Material.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener materiales: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener materiales", e);
            }
        });
    }

    public List<Material> getAllActivos() {
        return executeQueryList(em -> {
            try {
                return em.createQuery(
                        "SELECT m FROM Material m " +
                                "LEFT JOIN FETCH m.grupo " +
                                "LEFT JOIN FETCH m.unidad " +
                                "WHERE m.estado = 'Activo' " +
                                "ORDER BY m.nombre",
                        Material.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener materiales activos: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener materiales activos", e);
            }
        });
    }

    @Override
    public Material create(Material entity) {
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
            logger.error("Error al crear material: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear material", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Material update(Material entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Material updateObj = em.find(Material.class, entity.getId());

            updateObj.setCodcorrelativo(entity.getCodcorrelativo());
            updateObj.setNombre(entity.getNombre());
            updateObj.setDescripcion(entity.getDescripcion());
            updateObj.setObservacion(entity.getObservacion());
            updateObj.setGrupo(entity.getGrupo());
            updateObj.setUnidad(entity.getUnidad());

            t.begin();
            updateObj = em.merge(updateObj);
            t.commit();
            return updateObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al actualizar material: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar material", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Material delete(Material entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Material deleteObj = em.find(Material.class, entity.getId());
            deleteObj.setEstado(Material.Estado.Baja);

            t.begin();
            deleteObj = em.merge(deleteObj);
            t.commit();
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar material: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar material", e);
        } finally {
            closeEm(em);
        }
    }
}