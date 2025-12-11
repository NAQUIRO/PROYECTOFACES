#!/bin/bash
# Script para verificar la conexión a la base de datos
# Ejecutar en una terminal en la carpeta del proyecto

echo "========================================"
echo "Verificador de Conexión ProyectoFaces"
echo "========================================"
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}1. Verificando MySQL en puerto 3306...${NC}"
if lsof -Pi :3306 -sTCP:LISTEN -t >/dev/null 2>&1 ; then
    echo -e "${GREEN}✓ MySQL está corriendo${NC}"
else
    echo -e "${RED}✗ MySQL NO está corriendo${NC}"
    echo "   Solución: Abra XAMPP y haga click en 'Start' para MySQL"
fi
echo ""

echo -e "${YELLOW}2. Verificando que persistence.xml existe...${NC}"
if [ -f "src/main/resources/META-INF/persistence.xml" ]; then
    echo -e "${GREEN}✓ persistence.xml encontrado${NC}"
else
    echo -e "${RED}✗ persistence.xml NO encontrado en src/main/resources/META-INF/${NC}"
    echo "   Solución: Asegúrese de crear el archivo en la ruta correcta"
fi
echo ""

echo -e "${YELLOW}3. Compilando el proyecto...${NC}"
mvn clean compile 2>&1 | grep -E "(ERROR|BUILD)" || echo -e "${GREEN}✓ Compilación completada${NC}"
echo ""

echo -e "${YELLOW}4. Verificando dependencias en pom.xml...${NC}"
if grep -q "hibernate-core" pom.xml; then
    echo -e "${GREEN}✓ Hibernate Core está en pom.xml${NC}"
else
    echo -e "${RED}✗ Hibernate Core NO está en pom.xml${NC}"
fi

if grep -q "mysql-connector-j" pom.xml; then
    echo -e "${GREEN}✓ MySQL Connector está en pom.xml${NC}"
else
    echo -e "${RED}✗ MySQL Connector NO está en pom.xml${NC}"
fi
echo ""

echo "========================================"
echo "Verificación completada"
echo "========================================"
