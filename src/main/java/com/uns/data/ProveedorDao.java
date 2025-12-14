package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Proveedor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class ProveedorDao extends JPA implements DAO<Proveedor> {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorDao.class);

    @Override
    public Proveedor getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID inválido");
                }
                return em.find(Proveedor.class, id);
            } catch (Exception e) {
                logger.error("Error al buscar proveedor: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar proveedor", e);
            }
        });
    }

    @Override
    public List<Proveedor> getAll() {
        return executeQueryList(em -> {
            try {
                return em.createQuery("SELECT p FROM Proveedor p ORDER BY p.id DESC", Proveedor.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener proveedores: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener proveedores", e);
            }
        });
    }

    public List<Proveedor> getAllActivos() {
        return executeQueryList(em -> {
            try {
                return em.createQuery("SELECT p FROM Proveedor p WHERE p.estado = 'Activo' ORDER BY p.nombre", Proveedor.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener proveedores activos: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener proveedores activos", e);
            }
        });
    }

    @Override
    public Proveedor create(Proveedor entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            t.begin();
            em.persist(entity);
            t.commit();
            logger.info("Proveedor creado exitosamente: {}", entity.getNombre());
            return entity;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al crear proveedor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear proveedor", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Proveedor update(Proveedor entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Proveedor updateObj = em.find(Proveedor.class, entity.getId());
            if (updateObj == null) {
                throw new RuntimeException("Proveedor no encontrado: " + entity.getId());
            }
            
            updateObj.setRuc(entity.getRuc());
            updateObj.setNombre(entity.getNombre());
            updateObj.setDireccion(entity.getDireccion());
            updateObj.setTelefono(entity.getTelefono());
            updateObj.setCorreo(entity.getCorreo());
            updateObj.setContacto(entity.getContacto());
            updateObj.setCuentaBancaria(entity.getCuentaBancaria());

            t.begin();
            updateObj = em.merge(updateObj);
            t.commit();
            logger.info("Proveedor actualizado exitosamente: {}", updateObj.getNombre());
            return updateObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al actualizar proveedor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar proveedor", e);
        } finally {
            closeEm(em);
        }
    }

    @Override
    public Proveedor delete(Proveedor entity) {
        EntityManager em = getEm();
        EntityTransaction t = em.getTransaction();
        try {
            Proveedor deleteObj = em.find(Proveedor.class, entity.getId());
            if (deleteObj == null) {
                throw new RuntimeException("Proveedor no encontrado: " + entity.getId());
            }
            
            deleteObj.setEstado(Proveedor.Estado.Inactivo);

            t.begin();
            deleteObj = em.merge(deleteObj);
            t.commit();
            logger.info("Proveedor marcado como inactivo: {}", deleteObj.getNombre());
            return deleteObj;
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            logger.error("Error al eliminar proveedor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar proveedor", e);
        } finally {
            closeEm(em);
        }
    }
}