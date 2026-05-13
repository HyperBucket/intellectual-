# Start frontend dev server only (no backend required)
if (-not (Get-Command npm -ErrorAction SilentlyContinue)) {
    $env:PATH = "C:\Program Files\nodejs;$env:PATH"
}

Set-Location "$PSScriptRoot\frontend"
if (-not (Test-Path node_modules)) {
    Write-Host "Installing dependencies..." -ForegroundColor Cyan
    npm install
}
Write-Host "Starting frontend dev server on :5173 (backend not required)" -ForegroundColor Cyan
npm run dev
