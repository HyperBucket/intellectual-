# Start all services: Docker, backend, frontend
$root = $PSScriptRoot

if (-not (Get-Command npm -ErrorAction SilentlyContinue)) {
    $env:PATH = "C:\Program Files\nodejs;$env:PATH"
}

# Start Docker services
Write-Host "Starting Docker services..." -ForegroundColor Cyan
$running = docker ps --filter "name=intellectual-postgres" --filter "status=running" -q
if (-not $running) {
    docker start intellectual-postgres intellectual-redis 2>&1 | Out-Null
}

# Wait for healthy
Write-Host "Waiting for PostgreSQL and Redis to be healthy..." -ForegroundColor Cyan
$timeout = 30
$elapsed = 0
do {
    Start-Sleep -Seconds 2
    $elapsed += 2
    $pg = docker inspect --format="{{.State.Health.Status}}" intellectual-postgres 2>$null
    $rd = docker inspect --format="{{.State.Health.Status}}" intellectual-redis 2>$null
} while (($pg -ne "healthy" -or $rd -ne "healthy") -and $elapsed -lt $timeout)

if ($pg -ne "healthy" -or $rd -ne "healthy") {
    Write-Host "Docker services did not become healthy in time. Check: docker ps" -ForegroundColor Red
    exit 1
}
Write-Host "Docker services healthy" -ForegroundColor Green

# Start backend in a new window
Write-Host "Starting backend on :8080..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", `
    "Set-Location '$root'; Write-Host 'Backend starting...' -ForegroundColor Cyan; .\gradlew.bat :backend:bootRun"

# Start frontend in a new window
Write-Host "Starting frontend on :5173..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", `
    "Set-Location '$root\frontend'; Write-Host 'Frontend starting...' -ForegroundColor Cyan; npm run dev"

Write-Host ""
Write-Host "All services launching:" -ForegroundColor Green
Write-Host "  Frontend  ->  http://localhost:5173" -ForegroundColor White
Write-Host "  Backend   ->  http://localhost:8080" -ForegroundColor White
