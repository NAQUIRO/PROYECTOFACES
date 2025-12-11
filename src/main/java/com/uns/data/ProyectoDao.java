package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Proyecto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class ProyectoDao extends JPA implements DAO<Proyecto> {

    private static final Logger logger = LoggerFactory.getLogger(ProyectoDao.class);

    @Override
    public Proyecto getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Proyecto.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar proyecto: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar proyecto", e);
            }
        });
    }

    @Override
    public List<Proyecto> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery("SELECT p FROM Proyecto p ORDER BY p.id DESC", Proyecto.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener proyectos: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener proyectos", e);
            }
        });
    }

    public List<Proyecto> getAllActivos() {
        return executeQueryList(em -> {
            try {
                return em
                        .createQuery("SELECT p FROM Proyecto p WHERE p.estado = 'Activo' ORDER BY p.nombre",
                                Proyecto.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener proyectos activos: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener proyectos activos", e);
            }
        });
    }

    @Override
    public Proyecto create(Proyecto entity) {
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
            logger.error("Error al crear proyecto: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear proyecto", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Proyecto update(Proyecto entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Proyecto updateObj = em.find(Proyecto.class, entity.getId());
            updateObj.setCodigo(entity.getCodigo());
            updateObj.setNombre(entity.getNombre());
            updateObj.setArea(entity.getArea());
            updateObj.setDescripcion(entity.getDescripcion());
            updateObj.setFechaInicio(entity.getFechaInicio());
            updateObj.setFechaFin(entity.getFechaFin());

            t.begin();
            updateObj = em.merge(updateObj);
            t.commit();
            return updateObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al actualizar proyecto: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar proyecto", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Proyecto delete(Proyecto entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Proyecto deleteObj = em.find(Proyecto.class, entity.getId());
            deleteObj.setEstado(Proyecto.Estado.Inactivo);

            t.begin();
            deleteObj = em.merge(deleteObj);
            t.commit();
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar proyecto: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar proyecto", e);
        } finally {
            closeEm(em);
        }
    }
}