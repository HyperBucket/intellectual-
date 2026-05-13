# Build backend and frontend
$root = $PSScriptRoot

if (-not (Get-Command npm -ErrorAction SilentlyContinue)) {
    $env:PATH = "C:\Program Files\nodejs;$env:PATH"
}

Write-Host "=== Building backend ===" -ForegroundColor Cyan
Set-Location $root
.\gradlew.bat :backend:build
if ($LASTEXITCODE -ne 0) {
    Write-Host "Backend build FAILED" -ForegroundColor Red
    exit 1
}
Write-Host "Backend build OK" -ForegroundColor Green

Write-Host ""
Write-Host "=== Building frontend ===" -ForegroundColor Cyan
Set-Location "$root\frontend"
npm install
npm run build
if ($LASTEXITCODE -ne 0) {
    Write-Host "Frontend build FAILED" -ForegroundColor Red
    exit 1
}
Write-Host "Frontend build OK" -ForegroundColor Green

Write-Host ""
Write-Host "=== All builds successful ===" -ForegroundColor Green
