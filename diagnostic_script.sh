#!/bin/bash
# Script de diagnóstico completo para ProyectoFaces
# Ejecutar: bash diagnostic_script.sh

echo "========================================================"
echo "🔍 DIAGNÓSTICO COMPLETO - ProyectoFaces"
echo "========================================================"
echo ""

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Contador de errores
ERRORS=0
WARNINGS=0

echo -e "${BLUE}📋 1. VERIFICACIÓN DE ARCHIVOS CRÍTICOS${NC}"
echo "=========================================================="

# Archivos Java críticos
JAVA_FILES=(
    "src/main/java/com/uns/controllers/MaterialBean.java"
    "src/main/java/com/uns/controllers/RequerimientoBean.java"
    "src/main/java/com/uns/controllers/OrdenCompraBean.java"
    "src/main/java/com/uns/data/jpa/JPAFactory.java"
    "src/main/resources/META-INF/persistence.xml"
)

for file in "${JAVA_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}✓${NC} $file existe"
    else
        echo -e "${RED}✗${NC} $file NO EXISTE"
        ERRORS=$((ERRORS + 1))
    fi
done

echo ""
echo -e "${BLUE}📋 2. VERIFICACIÓN DE PÁGINAS XHTML${NC}"
echo "=========================================================="

# Páginas XHTML críticas
XHTML_FILES=(
    "src/main/webapp/pages/materiales/index.xhtml"
    "src/main/webapp/pages/materiales/add.xhtml"
    "src/main/webapp/pages/requerimientos/index.xhtml"
    "src/main/webapp/pages/ordenes/index.xhtml"
    "src/main/webapp/WEB-INF/templates/template.xhtml"
)

for file in "${XHTML_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}✓${NC} $file existe"
    else
        echo -e "${RED}✗${NC} $file NO EXISTE"
        ERRORS=$((ERRORS + 1))
    fi
done

echo ""
echo -e "${BLUE}📋 3. VERIFICACIÓN DE RUTAS EN BEANS${NC}"
echo "=========================================================="

# Verificar rutas incorrectas en MaterialBean
if grep -q "/pages/materiales/index.xhtml" "src/main/java/com/uns/controllers/MaterialBean.java" 2>/dev/null; then
    echo -e "${RED}✗${NC} MaterialBean usa rutas absolutas incorrectas"
    echo "   Cambiar '/pages/materiales/index.xhtml' por 'index'"
    ERRORS=$((ERRORS + 1))
else
    echo -e "${GREEN}✓${NC} MaterialBean usa rutas correctas"
fi

# Verificar rutas en RequerimientoBean
if grep -q "/pages/requerimientos/index.xhtml" "src/main/java/com/uns/controllers/RequerimientoBean.java" 2>/dev/null; then
    echo -e "${RED}✗${NC} RequerimientoBean usa rutas absolutas incorrectas"
    ERRORS=$((ERRORS + 1))
else
    echo -e "${GREEN}✓${NC} RequerimientoBean usa rutas correctas"
fi

# Verificar rutas en OrdenCompraBean
if grep -q "/pages/ordenes/index.xhtml" "src/main/java/com/uns/controllers/OrdenCompraBean.java" 2>/dev/null; then
    echo -e "${RED}✗${NC} OrdenCompraBean usa rutas absolutas incorrectas"
    ERRORS=$((ERRORS + 1))
else
    echo -e "${GREEN}✓${NC} OrdenCompraBean usa rutas correctas"
fi

echo ""
echo -e "${BLUE}📋 4. VERIFICACIÓN DE @PostConstruct${NC}"
echo "=========================================================="

# Verificar @PostConstruct en MaterialBean
if grep -q "@PostConstruct" "src/main/java/com/uns/controllers/MaterialBean.java" 2>/dev/null; then
    echo -e "${GREEN}✓${NC} MaterialBean tiene @PostConstruct"
else
    echo -e "${YELLOW}⚠${NC} MaterialBean no tiene @PostConstruct"
    WARNINGS=$((WARNINGS + 1))
fi

# Verificar @PostConstruct en OrdenCompraBean
if grep -q "@PostConstruct" "src/main/java/com/uns/controllers/OrdenCompraBean.java" 2>/dev/null; then
    echo -e "${GREEN}✓${NC} OrdenCompraBean tiene @PostConstruct"
else
    echo -e "${YELLOW}⚠${NC} OrdenCompraBean no tiene @PostConstruct"
    WARNINGS=$((WARNINGS + 1))
fi

echo ""
echo -e "${BLUE}📋 5. VERIFICACIÓN DE AJAX EN XHTML${NC}"
echo "=========================================================="

# Verificar ajax="false" en index.xhtml de materiales
if grep -q 'ajax="false"' "src/main/webapp/pages/materiales/index.xhtml" 2>/dev/null; then
    echo -e "${GREEN}✓${NC} materiales/index.xhtml usa ajax=false en botones"
else
    echo -e "${YELLOW}⚠${NC} materiales/index.xhtml podría necesitar ajax=false"
    WARNINGS=$((WARNINGS + 1))
fi

echo ""
echo -e "${BLUE}📋 6. VERIFICACIÓN DE MYSQL${NC}"
echo "=========================================================="

# Verificar MySQL en Windows
if command -v mysql &> /dev/null; then
    if mysql -u root -e "SELECT 1" &> /dev/null; then
        echo -e "${GREEN}✓${NC} MySQL está accesible"
        
        # Verificar base de datos
        if mysql -u root -e "USE dbprueba" &> /dev/null; then
            echo -e "${GREEN}✓${NC} Base de datos 'dbprueba' existe"
        else
            echo -e "${RED}✗${NC} Base de datos 'dbprueba' NO existe"
            ERRORS=$((ERRORS + 1))
        fi
    else
        echo -e "${RED}✗${NC} MySQL no está accesible"
        ERRORS=$((ERRORS + 1))
    fi
else
    echo -e "${YELLOW}⚠${NC} Comando mysql no encontrado (probablemente en Windows)"
    echo "   Verificar manualmente en XAMPP o phpMyAdmin"
fi

echo ""
echo -e "${BLUE}📋 7. VERIFICACIÓN DE POM.XML${NC}"
echo "=========================================================="

# Verificar dependencias críticas
if grep -q "hibernate-core" "pom.xml" 2>/dev/null; then
    echo -e "${GREEN}✓${NC} Hibernate Core está en pom.xml"
else
    echo -e "${RED}✗${NC} Hibernate Core NO está en pom.xml"
    ERRORS=$((ERRORS + 1))
fi

if grep -q "mysql-connector-j" "pom.xml" 2>/dev/null; then
    echo -e "${GREEN}✓${NC} MySQL Connector está en pom.xml"
else
    echo -e "${RED}✗${NC} MySQL Connector NO está en pom.xml"
    ERRORS=$((ERRORS + 1))
fi

if grep -q "primefaces" "pom.xml" 2>/dev/null; then
    echo -e "${GREEN}✓${NC} PrimeFaces está en pom.xml"
else
    echo -e "${RED}✗${NC} PrimeFaces NO está en pom.xml"
    ERRORS=$((ERRORS + 1))
fi

echo ""
echo "========================================================"
echo -e "${BLUE}📊 RESUMEN DEL DIAGNÓSTICO${NC}"
echo "========================================================"
echo ""

if [ $ERRORS -eq 0 ] && [ $WARNINGS -eq 0 ]; then
    echo -e "${GREEN}✅ TODO ESTÁ CORRECTO${NC}"
    echo ""
    echo "Pasos siguientes:"
    echo "1. mvn clean compile"
    echo "2. Desplegar en Tomcat"
    echo "3. Acceder a http://localhost:8080/proyectoFaces"
elif [ $ERRORS -eq 0 ]; then
    echo -e "${YELLOW}⚠️ ${WARNINGS} ADVERTENCIA(S)${NC}"
    echo ""
    echo "El proyecto debería funcionar, pero revisa las advertencias."
else
    echo -e "${RED}❌ ${ERRORS} ERROR(ES) CRÍTICO(S)${NC}"
    echo -e "${YELLOW}⚠️ ${WARNINGS} ADVERTENCIA(S)${NC}"
    echo ""
    echo "ACCIONES REQUERIDAS:"
    echo ""
    
    if [ $ERRORS -gt 0 ]; then
        echo "1. Corregir los errores marcados con ✗"
        echo "2. Aplicar los archivos corregidos proporcionados"
        echo "3. Verificar que MySQL esté corriendo"
        echo "4. Compilar: mvn clean compile"
    fi
fi

echo ""
echo "========================================================"
echo -e "${BLUE}🔧 ARCHIVOS CORREGIDOS DISPONIBLES${NC}"
echo "========================================================"
echo ""
echo "Se han generado los siguientes archivos corregidos:"
echo "- MaterialBean.java"
echo "- RequerimientoBean.java"
echo "- OrdenCompraBean.java"
echo "- materiales/index.xhtml"
echo "- template.xhtml (sección del menú)"
echo ""
echo "Aplica estos archivos y ejecuta:"
echo "  mvn clean compile"
echo ""
