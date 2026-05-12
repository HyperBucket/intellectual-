# Build backend only
Set-Location $PSScriptRoot
Write-Host "=== Building backend ===" -ForegroundColor Cyan
.\gradlew.bat :backend:build
if ($LASTEXITCODE -ne 0) {
    Write-Host "Backend build FAILED" -ForegroundColor Red
    exit 1
}
Write-Host "Backend build OK" -ForegroundColor Green
