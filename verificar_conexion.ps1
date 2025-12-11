# Script para verificar la conexión a la base de datos en Windows
# Ejecutar en PowerShell como administrador

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Verificador de Conexión ProyectoFaces" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar MySQL
Write-Host "1. Verificando MySQL en puerto 3306..." -ForegroundColor Yellow
$mysql_check = Get-NetTCPConnection -LocalPort 3306 -ErrorAction SilentlyContinue
if ($mysql_check) {
    Write-Host "✓ MySQL está corriendo" -ForegroundColor Green
} else {
    Write-Host "✗ MySQL NO está corriendo" -ForegroundColor Red
    Write-Host "   Solución: Abra XAMPP y haga click en 'Start' para MySQL"
}
Write-Host ""

# Verificar persistence.xml
Write-Host "2. Verificando que persistence.xml existe..." -ForegroundColor Yellow
$persistence_path = "src\main\resources\META-INF\persistence.xml"
if (Test-Path $persistence_path) {
    Write-Host "✓ persistence.xml encontrado" -ForegroundColor Green
    Write-Host "   Ruta: $persistence_path" -ForegroundColor Green
} else {
    Write-Host "✗ persistence.xml NO encontrado" -ForegroundColor Red
    Write-Host "   Ruta esperada: $persistence_path" -ForegroundColor Red
}
Write-Host ""

# Verificar pom.xml
Write-Host "3. Verificando dependencias en pom.xml..." -ForegroundColor Yellow
$pom_content = Get-Content pom.xml -Raw

if ($pom_content -match "hibernate-core") {
    Write-Host "✓ Hibernate Core está en pom.xml" -ForegroundColor Green
} else {
    Write-Host "✗ Hibernate Core NO está en pom.xml" -ForegroundColor Red
}

if ($pom_content -match "mysql-connector-j") {
    Write-Host "✓ MySQL Connector está en pom.xml" -ForegroundColor Green
} else {
    Write-Host "✗ MySQL Connector NO está en pom.xml" -ForegroundColor Red
}
Write-Host ""

# Verificar estructura de carpetas
Write-Host "4. Verificando estructura de carpetas..." -ForegroundColor Yellow
$folders = @(
    "src\main\java\com\uns\controllers",
    "src\main\java\com\uns\data",
    "src\main\java\com\uns\entities",
    "src\main\resources\META-INF"
)

foreach ($folder in $folders) {
    if (Test-Path $folder) {
        Write-Host "✓ $folder existe" -ForegroundColor Green
    } else {
        Write-Host "✗ $folder NO existe" -ForegroundColor Red
    }
}
Write-Host ""

# Mensaje final
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Verificación completada" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Próximos pasos:" -ForegroundColor Yellow
Write-Host "1. Asegurar MySQL está corriendo en XAMPP"
Write-Host "2. Importar el script SQL en phpMyAdmin"
Write-Host "3. Compilar: mvn clean compile"
Write-Host "4. Ejecutar en Tomcat"
Write-Host "5. Revisar los logs para errores"
