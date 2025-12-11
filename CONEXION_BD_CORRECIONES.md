# Correcciones de Conexión a Base de Datos - ProyectoFaces

## ✓ Cambios Realizados

### 1. **persistence.xml** (CRÍTICO)
- **Ubicación correcta**: `src/main/resources/META-INF/persistence.xml`
- **Configuración Hibernate**: Agregada la configuración completa de Hibernate 6.2
- **Dialecto MySQL**: `org.hibernate.dialect.MySQL8Dialect`
- **URL de conexión**: `jdbc:mysql://127.0.0.1:3306/dbprueba`
- **Pool de conexiones**: Configurado con C3P0 para mejor manejo de conexiones

### 2. **JPAFactory.java** (MEJORADO)
- Inicialización thread-safe con sincronización
- Mejor manejo de excepciones con mensajes detallados
- Logging mejorado para diagnóstico
- Reinicialización automática si la conexión se pierde

### 3. **LoginBean.java** (MEJORADO)
- Logging detallado de cada paso del login
- Mensajes de error más informativos
- Stack traces completos en logs
- Validación robusta de usuario y contraseña

### 4. **UsuarioDao.java** (ESTANDARIZADO)
- Patrón consistente de manejo de transacciones
- LEFT JOIN FETCH para cargar relaciones de Rol y Área
- Eliminación lógica (cambiar estado a Inactivo)
- Logging en cada operación

---

## 🔍 Verificación de la Instalación

### Paso 1: Verificar MySQL está corriendo
```powershell
# Abrir XAMPP y verificar que MySQL esté activo (puerto 3306)
# O ejecutar en PowerShell:
netstat -ano | findstr :3306
```

### Paso 2: Verificar la base de datos existe
```sql
-- Abrir phpMyAdmin o MySQL Workbench:
-- URL: http://localhost/phpmyadmin
-- O ejecutar en MySQL:
SHOW DATABASES;
USE dbprueba;
SHOW TABLES;
SELECT * FROM usuario;
```

### Paso 3: Verificar los datos de usuario
```sql
-- Los usuarios de prueba deben existir:
SELECT username, password, estado FROM usuario;

-- Usuarios esperados:
-- admin / 123456 / Activo
-- jlopez / 123456 / Activo
-- mgarcia / 123456 / Activo
-- rmorales / 123456 / Activo
-- lsanchez / 123456 / Activo
```

### Paso 4: Compilar el proyecto
```powershell
cd C:\Users\aanto\OneDrive\Documents\NetBeansProjects\proyectoFaces
mvn clean compile
# o si está en NetBeans: Click derecho en proyecto > Build
```

### Paso 5: Revisar los logs de inicio
- Buscar mensajes que comienzan con "✓" (éxito) o "✗" (error)
- Los logs deben mostrar:
  ```
  ✓ EntityManagerFactory inicializado correctamente
  ✓ EntityManager de prueba creado exitosamente
  ✓ Conexión a base de datos verificada
  ```

---

## 🐛 Solución de Problemas

### Error: "No se pudo inicializar la conexión a la base de datos"
**Causa probable**: MySQL no está corriendo
**Solución**:
1. Abrir XAMPP Control Panel
2. Click en "Start" para MySQL
3. Esperar a que esté en verde y reintentar

### Error: "Unknown database 'dbprueba'"
**Causa probable**: Base de datos no importada
**Solución**:
1. Abrir phpMyAdmin: http://localhost/phpmyadmin
2. Click en "Import" (Importar)
3. Seleccionar el script SQL proporcionado
4. Click en "Go"

### Error: "Access denied for user 'root'@'localhost'"
**Causa probable**: Contraseña diferente
**Solución**: Editar `src/main/resources/META-INF/persistence.xml`:
```xml
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value="tu_contraseña"/>
```

### Login no responde
**Causa probable**: EntityManagerFactory no inicializado
**Solución**:
1. Revisar los logs de consola de Tomcat
2. Buscar líneas con "ERROR" o "CRÍTICO"
3. Asegurarse que persistence.xml esté en la ruta correcta

---

## 📋 Checklist Final

- [ ] MySQL está corriendo en puerto 3306
- [ ] Base de datos `dbprueba` existe
- [ ] Tabla `usuario` contiene datos
- [ ] Archivo `persistence.xml` está en `src/main/resources/META-INF/`
- [ ] Proyecto compiló sin errores
- [ ] Los logs muestran "✓" para EntityManagerFactory
- [ ] Puedes hacer login con usuario: admin, contraseña: 123456

---

## 📝 Notas Importantes

1. **Credenciales de prueba** (en la BD):
   - Usuario: `admin` | Contraseña: `123456`
   - Usuario: `jlopez` | Contraseña: `123456`

2. **Ubicación del persistence.xml**:
   - ❌ Incorrecto: `src/resources/META-INF/persistence.xml`
   - ✅ Correcto: `src/main/resources/META-INF/persistence.xml`

3. **Dependencias necesarias** (ya en pom.xml):
   - Hibernate ORM 6.2.7
   - MySQL Connector 8.3.0
   - Jakarta EE 10.0.0

4. **Si tienes más problemas**:
   - Revisar la consola de Tomcat en NetBeans
   - Buscar líneas con "ERROR", "WARN" o "CRÍTICO"
   - Los logs muestran exactamente dónde falla la conexión

---

## 🚀 Próximos Pasos

Una vez que el login funcione:
1. Probar la navegación del dashboard
2. Verificar que los datos se cargan correctamente en las listas
3. Probar las operaciones CRUD (Create, Read, Update, Delete)
4. Revisar los logs para asegurarse que no hay errores

¡Éxito! 🎉
