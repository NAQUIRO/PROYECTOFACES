package com.uns.data.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JPAFactory {

    private static final Logger logger = LoggerFactory.getLogger(JPAFactory.class);
    private static final String PERSISTENCE_UNIT_NAME = "bdjpa";
    private static EntityManagerFactory entityManagerFactory;
    private static boolean initialized = false;

    static {
        initializeFactory();
    }

    private static synchronized void initializeFactory() {
        if (initialized) {
            return;
        }

        try {
            logger.info("========================================");
            logger.info("Inicializando EntityManagerFactory...");
            logger.info("Unidad de persistencia: {}", PERSISTENCE_UNIT_NAME);
            logger.info("========================================");

            // Propiedades de configuración
            Map<String, String> properties = new HashMap<>();
            properties.put("hibernate.show_sql", "true");
            properties.put("hibernate.format_sql", "true");
            properties.put("hibernate.use_sql_comments", "true");
            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            // Evita que Hibernate registre el transformador de bytecode que estaba generando errores de carga circular
            properties.put("hibernate.bytecode.provider", "none");


            entityManagerFactory = Persistence.createEntityManagerFactory(
                    PERSISTENCE_UNIT_NAME,
                    properties);

            if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
                throw new RuntimeException("EntityManagerFactory no se pudo crear o está cerrado");
            }

            logger.info("========================================");
            logger.info("✓ EntityManagerFactory inicializado correctamente");
            logger.info("========================================");

            // Verificar conexión
            EntityManager testEm = null;
            try {
                testEm = entityManagerFactory.createEntityManager();
                logger.info("✓ EntityManager de prueba creado exitosamente");
                
                // Probar una consulta simple
                testEm.getTransaction().begin();
                Long count = testEm.createQuery("SELECT COUNT(u) FROM Unidad u", Long.class)
                    .getSingleResult();
                testEm.getTransaction().commit();
                logger.info("✓ Consulta de prueba exitosa. Unidades en BD: {}", count);
                
            } catch (Exception e) {
                logger.error("✗ Error en consulta de prueba: {}", e.getMessage());
                if (testEm != null && testEm.getTransaction().isActive()) {
                    testEm.getTransaction().rollback();
                }
                throw e;
            } finally {
                if (testEm != null && testEm.isOpen()) {
                    testEm.close();
                }
            }

            initialized = true;

        } catch (Exception e) {
            logger.error("========================================");
            logger.error("✗ ERROR CRÍTICO al inicializar EntityManagerFactory");
            logger.error("========================================");
            logger.error("Tipo de error: {}", e.getClass().getName());
            logger.error("Mensaje: {}", e.getMessage());
            
            if (e.getCause() != null) {
                logger.error("Causa: {}", e.getCause().getMessage());
                logger.error("Clase causa: {}", e.getCause().getClass().getName());
            }
            
            logger.error("========================================");
            logger.error("VERIFICAR:");
            logger.error("1. ✓ MySQL está corriendo (puerto 3306)");
            logger.error("2. ✓ La base de datos 'dbprueba' existe");
            logger.error("3. ✓ El usuario 'root' tiene permisos");
            logger.error("4. ✓ Las tablas existen en la BD");
            logger.error("========================================");
            
            logger.error("Stack trace:", e);
            
            throw new ExceptionInInitializerError(
                    "No se pudo inicializar la conexión a la base de datos: " + e.getMessage());
        }
    }

    public static EntityManager createEntityManager() {
        try {
            if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
                logger.warn("EntityManagerFactory estaba nulo o cerrado, reinicializando...");
                synchronized (JPAFactory.class) {
                    if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
                        initialized = false;
                        initializeFactory();
                    }
                }
            }

            EntityManager em = entityManagerFactory.createEntityManager();
            logger.debug("EntityManager creado exitosamente");
            return em;

        } catch (Exception e) {
            logger.error("========================================");
            logger.error("✗ ERROR al crear EntityManager");
            logger.error("========================================");
            logger.error("Mensaje: {}", e.getMessage());
            logger.error("Clase: {}", e.getClass().getName());
            logger.error("Stack trace:", e);
            throw new RuntimeException("Error al crear EntityManager: " + e.getMessage(), e);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            synchronized (JPAFactory.class) {
                if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
                    initialized = false;
                    initializeFactory();
                }
            }
        }
        return entityManagerFactory;
    }

    public static void close() {
        try {
            if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
                logger.info("✓ EntityManagerFactory cerrado correctamente");
            }
        } catch (Exception e) {
            logger.error("Error al cerrar EntityManagerFactory: {}", e.getMessage(), e);
        }
    }
}