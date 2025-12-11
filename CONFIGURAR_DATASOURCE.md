# 🔧 CONFIGURAR DATASOURCE JNDI EN PAYARA SERVER

## ⚠️ Problema Actual

Payara no encuentra el datasource JNDI `jdbc/dbprueba`:
```
JNDI lookup failed for the resource: Name: bdjpa, Lookup: jdbc/dbprueba
```

## ✅ Solución: Crear el Datasource Manualmente

### Opción 1: Script Automático (Recomendado) 👍

1. **Abre PowerShell como Administrador**
2. **Ejecuta el script:**
   ```powershell
   cd 'C:\Users\aanto\OneDrive\Documents\NetBeansProjects\proyectoFaces'
   .\crear_datasource_payara.bat
   ```
3. **El script creará:**
   - ✓ Pool de conexiones: `MySQLPool`
   - ✓ Recurso JNDI: `jdbc/dbprueba`

### Opción 2: Comando Manual

Si prefieres hacerlo manualmente, abre PowerShell como Administrador:

```powershell
$ASADMIN = "C:\Users\aanto\Payara_Server\bin\asadmin.bat"

# Crear el pool
& $ASADMIN create-jdbc-connection-pool `
    --datasourceclassname=com.mysql.cj.jdbc.MysqlDataSource `
    --restype=javax.sql.DataSource `
    --property=serverName=localhost:portNumber=3306:databaseName=dbprueba:user=root:password=:characterEncoding=UTF-8:serverTimezone=America/Lima:allowPublicKeyRetrieval=true:useSSL=false `
    MySQLPool

# Crear el recurso JNDI
& $ASADMIN create-jdbc-resource `
    --connectionpoolid=MySQLPool `
    jdbc/dbprueba
```

### Opción 3: Admin Console de Payara

1. Abre: `http://localhost:4848`
2. Usuario: `admin`
3. Contraseña: (vacío, solo presiona Enter)
4. **Resources > JDBC > Connection Pools**
5. **New...**
   - Name: `MySQLPool`
   - Resource Type: `javax.sql.DataSource`
   - Database Driver Vendor: `MySQL`
6. **Next**
7. Agrega las propiedades:
   - `serverName=localhost`
   - `portNumber=3306`
   - `databaseName=dbprueba`
   - `user=root`
   - `password=` (vacío)
   - `allowPublicKeyRetrieval=true`
   - `useSSL=false`
8. **Finish**
9. **Resources > JDBC > JDBC Resources**
10. **New...**
    - JNDI Name: `jdbc/dbprueba`
    - Pool Name: `MySQLPool`
11. **OK**

---

## 🚀 Después de Crear el Datasource

1. **En NetBeans:**
   - Click derecho en Payara Server (Services)
   - **Stop Server**
   - Espera 10 segundos
   - **Start Server**

2. **Espera a que Payara inicie** (30-60 segundos)

3. **En el navegador:**
   ```
   http://localhost:8080/proyectoFaces
   ```

4. **Login con:**
   - Usuario: `admin`
   - Contraseña: `123456`

---

## ✅ Verificar que el Datasource Funciona

En los logs de Payara deberías ver:

```
✓ HHH000204: Processing PersistenceUnitInfo [name: bdjpa]
✓ HHH000412: Hibernate ORM core version 6.2.7.Final
✓ Loading application [proyectoFaces]
```

**Sin errores GRAVE o Exception**

---

## 🆘 Si Aún No Funciona

1. **Verifica que MySQL está corriendo:**
   ```powershell
   netstat -ano | findstr :3306
   ```

2. **Verifica la base de datos:**
   ```
   http://localhost/phpmyadmin
   Buscar tabla: usuario
   ```

3. **En Admin Console de Payara (http://localhost:4848):**
   - Monitoring > JDBC Connection Pools > MySQLPool
   - Click en **Ping** para probar la conexión

4. **Si sigue sin funcionar:**
   - Comparte los logs de Payara
   - Usaremos RESOURCE_LOCAL como alternativa

---

## 📝 Archivos Modificados

- `src/main/resources/META-INF/persistence.xml` - Cambio a JTA
- `src/main/webapp/WEB-INF/glassfish-resources.xml` - Configuración del datasource
- `crear_datasource_payara.bat` - Script para crear el datasource

---

## 💡 Nota Técnica

Estamos usando:
- **JTA** = Gestor de transacciones de Payara
- **JNDI** = Lookup remoto de recursos
- **Datasource JNDI** = Conexión administrada por Payara

Esto es lo correcto para un servidor de aplicaciones.
