package com.uns.data;

import com.uns.data.jpa.JPA;
import com.uns.entities.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class UsuarioDao extends JPA implements DAO<Usuario> {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioDao.class);

    /**
     * Busca un usuario por nombre de usuario
     * @param username nombre de usuario
     * @return Usuario si existe, null si no
     */
    public Usuario findByUsername(String username) {
        return executeQuery(em -> {
            try {
                logger.debug("Buscando usuario con username: {}", username);
                Usuario result = em.createQuery(
                        "SELECT u FROM Usuario u " +
                                "LEFT JOIN FETCH u.rol " +
                                "LEFT JOIN FETCH u.area " +
                                "WHERE u.username = :username",
                        Usuario.class)
                        .setParameter("username", username)
                        .getSingleResult();
                logger.debug("✓ Usuario encontrado: {} (ID: {})", result.getUsername(), result.getId());
                return result;
            } catch (NoResultException e) {
                logger.debug("Usuario no encontrado: {}", username);
                return null;
            } catch (Exception e) {
                logger.error("Error al buscar usuario por username: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar usuario", e);
            }
        });
    }

    @Override
    public Usuario getById(Long id) {
        return executeQuery(em -> {
            try {
                if (id == null || id <= 0) {
                    throw new IllegalArgumentException("ID de usuario inválido");
                }
                logger.debug("Buscando usuario por ID: {}", id);
                Usuario usuario = em.find(Usuario.class, id);
                if (usuario != null) {
                    // Inicializar relaciones lazy
                    if (usuario.getRol() != null) {
                        usuario.getRol().getId();
                    }
                    if (usuario.getArea() != null) {
                        usuario.getArea().getId();
                    }
                }
                return usuario;
            } catch (Exception e) {
                logger.error("Error al buscar usuario por ID: {}", e.getMessage(), e);
                throw new RuntimeException("Error al buscar usuario", e);
            }
        });
    }

    @Override
    public List<Usuario> getAll() {
        return executeQueryList(em -> {
            try {
                logger.debug("Obteniendo todos los usuarios");
                return em.createQuery(
                        "SELECT u FROM Usuario u " +
                                "LEFT JOIN FETCH u.rol " +
                                "LEFT JOIN FETCH u.area " +
                                "ORDER BY u.id DESC",
                        Usuario.class)
                        .getResultList();
            } catch (Exception e) {
                logger.error("Error al obtener usuarios: {}", e.getMessage(), e);
                throw new RuntimeException("Error al obtener usuarios", e);
            }
        });
    }

    @Override
    public Usuario create(Usuario entity) {
        return executeQuery(em -> {
            try {
                logger.info("Creando nuevo usuario: {}", entity.getUsername());
                em.getTransaction().begin();
                em.persist(entity);
                em.getTransaction().commit();
                logger.info("✓ Usuario creado exitosamente: {}", entity.getId());
                return entity;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                logger.error("Error al crear usuario: {}", e.getMessage(), e);
                throw new RuntimeException("Error al crear usuario", e);
            }
        });
    }

    @Override
    public Usuario update(Usuario entity) {
        return executeQuery(em -> {
            try {
                logger.info("Actualizando usuario: {}", entity.getId());
                em.getTransaction().begin();
                Usuario merged = em.merge(entity);
                em.getTransaction().commit();
                logger.info("✓ Usuario actualizado exitosamente: {}", entity.getId());
                return merged;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                logger.error("Error al actualizar usuario: {}", e.getMessage(), e);
                throw new RuntimeException("Error al actualizar usuario", e);
            }
        });
    }

    @Override
    public Usuario delete(Usuario entity) {
        return executeQuery(em -> {
            try {
                logger.info("Desactivando usuario: {}", entity.getId());
                em.getTransaction().begin();
                Usuario ref = em.find(Usuario.class, entity.getId());
                if (ref != null) {
                    // Eliminación lógica (cambiar estado a Inactivo)
                    ref.setEstado(Usuario.Estado.Inactivo);
                    em.merge(ref);
                    logger.info("✓ Usuario desactivado exitosamente: {}", entity.getId());
                } else {
                    logger.warn("Usuario no encontrado para desactivar: {}", entity.getId());
                }
                em.getTransaction().commit();
                return ref;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                logger.error("Error al desactivar usuario: {}", e.getMessage(), e);
                throw new RuntimeException("Error al desactivar usuario", e);
            }
        });
    }
}