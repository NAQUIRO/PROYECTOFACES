# 🚀 GUÍA DE EJECUCIÓN RÁPIDA

## ⚡ En 5 pasos simples

### Paso 1️⃣: Iniciar MySQL en XAMPP
1. Abrir **XAMPP Control Panel**
2. Buscar **MySQL**
3. Click en **"Start"**
4. Esperar a que esté en verde y muestre el puerto 3306

### Paso 2️⃣: Importar la Base de Datos
1. Abrir navegador: `http://localhost/phpmyadmin`
2. Click en **"New"** (Nuevo)
3. Nombre: `dbprueba`
4. Click en **"Create"**
5. Seleccionar la BD `dbprueba`
6. Click en **"Import"** (Importar)
7. Click en **"Choose File"** (Seleccionar archivo)
8. Seleccionar el script SQL que proporcionaste
9. Click en **"Import"** (abajo)
10. Esperar a que termine ✓

### Paso 3️⃣: Compilar el Proyecto ✓ (YA HECHO)
```powershell
# Ya compiló exitosamente, pero si necesitas recompilar:
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21'
cd 'C:\Users\aanto\OneDrive\Documents\NetBeansProjects\proyectoFaces'
.\mvnw.cmd compile
```

### Paso 4️⃣: Ejecutar en NetBeans
1. Abrir **NetBeans**
2. File > Open Project
3. Seleccionar: `C:\Users\aanto\OneDrive\Documents\NetBeansProjects\proyectoFaces`
4. Click derecho en el proyecto
5. **Run** (o presionar **F6**)
6. Esperar a que Tomcat inicie

### Paso 5️⃣: Acceder a la Aplicación
1. Abrir navegador: `http://localhost:8080/proyectoFaces`
2. **Usuario**: `admin`
3. **Contraseña**: `123456`
4. ¡Click en Enter! ✓

---

## ✅ Verificación Rápida

Después del Paso 4, deberías ver en la consola de NetBeans:

```
========================================
✓ EntityManagerFactory inicializado correctamente
✓ EntityManager de prueba creado exitosamente
✓ Conexión a base de datos verificada
========================================
```

Después del Paso 5 y hacer login, deberías ver:

```
========================================
✓ LOGIN EXITOSO para usuario: admin
Rol: Administrador
Área: Dirección General
========================================
```

---

## 🆘 Si Algo Sale Mal

### ❌ Error: "Access to the specified database is denied"
**Solución**: Reimportar el script SQL en phpMyAdmin

### ❌ Error: "Communication link failure" / MySQL no responde
**Solución**: 
1. Detener MySQL en XAMPP
2. Esperar 5 segundos
3. Volver a iniciar MySQL
4. Reintentar

### ❌ Error: "BUILD FAILURE" al compilar
**Solución**: Ejecutar con JAVA_HOME configurado:
```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21'
.\mvnw.cmd clean compile
```

### ❌ La página no carga (http://localhost:8080/proyectoFaces)
**Solución**:
1. Esperar 30 segundos a que Tomcat inicie
2. Revisar la consola de NetBeans por errores rojos
3. Si hay error, mostrar captura de pantalla

### ❌ Login responde pero no autentica
**Solución**: 
1. Ir a phpMyAdmin
2. Ejecutar: `SELECT * FROM usuario;`
3. Verificar que exista el usuario `admin` con contraseña `123456`

---

## 📋 Checklist

Antes de empezar, verifica:

- [ ] XAMPP está instalado
- [ ] NetBeans está instalado
- [ ] Tengo el script SQL proporcionado
- [ ] Java está configurado (JDK 21 o superior)

Durante la ejecución:

- [ ] MySQL está corriendo (verde en XAMPP)
- [ ] Base de datos `dbprueba` fue importada
- [ ] El proyecto compiló sin errores
- [ ] Tomcat inició correctamente
- [ ] Puedo acceder a http://localhost:8080/proyectoFaces

---

## 👤 Datos de Acceso para Pruebas

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| admin | 123456 | Administrador |
| jlopez | 123456 | Jefe de Área |
| mgarcia | 123456 | Encargado de Pedidos |
| rmorales | 123456 | Compras |

---

## 📞 Notas Importantes

1. **Si MySQL no inicia**: Reinicia tu computadora y vuelve a intentar
2. **Si NetBeans tarda en iniciar**: Es normal, solo espera
3. **Si ves un error rojo en consola**: Cópialo y revisa contra los logs
4. **Los datos de prueba están en la BD**: No necesitas crear usuarios manualmente

---

## ✨ ¡Listo!

Después de estos 5 pasos deberías poder:

✅ Iniciar sesión con `admin` / `123456`  
✅ Ver el dashboard  
✅ Navegar por las diferentes secciones  
✅ Crear, editar y eliminar registros  

Si algo no funciona, revisa los logs en la consola - usualmente el error está claramente descrito ahí.

¡Éxito! 🎉
