# Service Port Reference

> Always use `127.0.0.1` instead of `localhost` on Windows (avoids IPv6 routing issues with Docker Desktop).

| Service            | URL / Address             | Who uses it                  |
|--------------------|---------------------------|------------------------------|
| MinIO S3 API       | http://127.0.0.1:9000     | Spring Boot (`application.yml`) |
| MinIO Web UI       | http://127.0.0.1:9090     | Browser — login: `intellectual` / `intellectual123` |
| PostgreSQL         | 127.0.0.1:5432            | Spring Boot / DBeaver        |
| Redis              | 127.0.0.1:6379            | Spring Boot / RedisInsight   |
| Spring Backend     | http://127.0.0.1:8080     | Vue frontend (proxied via Vite) |
| Vue Dev Server     | http://127.0.0.1:5173     | Browser (main app URL)       |
