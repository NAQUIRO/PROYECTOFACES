package com.uns.data.jpa;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;

/**
 * Clase base para DAOs que proporciona acceso al EntityManager con gestión
 * automática de recursos.
 * Utiliza el patrón Template Method para asegurar que los EntityManagers se
 * cierren correctamente.
 */
public class JPA {

    /**
     * Ejecuta una operación de consulta que retorna un resultado único.
     * El EntityManager se cierra automáticamente después de la operación.
     * 
     * @param <T>       Tipo del resultado
     * @param operation Función que ejecuta la consulta
     * @return Resultado de la consulta
     */
    protected <T> T executeQuery(Function<EntityManager, T> operation) {
        EntityManager em = JPAFactory.createEntityManager();
        try {
            return operation.apply(em);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Ejecuta una operación de consulta que retorna una lista.
     * El EntityManager se cierra automáticamente después de la operación.
     * 
     * @param <T>       Tipo de los elementos de la lista
     * @param operation Función que ejecuta la consulta
     * @return Lista de resultados
     */
    protected <T> List<T> executeQueryList(Function<EntityManager, List<T>> operation) {
        EntityManager em = JPAFactory.createEntityManager();
        try {
            return operation.apply(em);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Crea y retorna un nuevo EntityManager.
     * IMPORTANTE: El DAO debe cerrar el EntityManager después de usarlo.
     * Se recomienda usar executeQuery o executeQueryList en su lugar.
     * 
     * @return EntityManager nuevo
     * @deprecated Usar executeQuery o executeQueryList para gestión automática
     */
    @Deprecated
    protected EntityManager getEm() {
        return JPAFactory.createEntityManager();
    }

    /**
     * Cierra un EntityManager específico.
     * 
     * @param em EntityManager a cerrar
     */
    protected void closeEm(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
