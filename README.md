# CareRide

Spring Boot 3.2.x / Java 17 backend for caretakers who book patient transport to and from hospitals.

## Features

- `POST /api/auth/signup` — registers a `CARETAKER` (role is never accepted from the request)
- `POST /api/auth/login` — returns a signed JWT
- `GET /api/hospitals` — lists hospitals
- `GET /api/slots?date=yyyy-MM-dd` — lists future pickup slots with remaining capacity
- `POST /api/bookings` — books one ride or a round trip for a patient

No cancel, reschedule, payments, driver app, tracking, or notifications.

## Prerequisites

- JDK 17
- Maven (or the included `./mvnw` wrapper)
- PostgreSQL database (the Neon pooled connection is configured by default)
- Docker with Compose (optional)

## Environment variables

| Variable | Required | Example |
|---|---|---|
| `JWT_SECRET` | production yes, dev optional | `openssl rand -base64 48` (must decode to at least 32 bytes) |
| `JWT_EXPIRATION_MS` | no | `3600000` |
| `DB_URL` | no | Neon JDBC URL without `user` or `password` parameters |
| `DB_USERNAME` | no | `neondb_owner` |
| `DB_PASSWORD` | yes | set it in your shell, never in source control |

If `JWT_SECRET` is not set, the app logs a warning and uses an ephemeral key: it starts, but tokens stop working after a restart. Always set it in production. In IntelliJ, add `JWT_SECRET` under Run → Edit Configurations → Environment variables.

Set the database credentials through `DB_USERNAME` / `DB_PASSWORD`; do not put `user=` or `password=` in `DB_URL`.

## Run locally

```bash
export JWT_SECRET=$(openssl rand -base64 48)
read -s -p "Neon DB password: " DB_PASSWORD; export DB_PASSWORD

./mvnw spring-boot:run
```

Or build and run the jar:

```bash
./mvnw clean package
java -jar target/care-ride-0.0.1-SNAPSHOT.jar
```

Flyway applies `V1__init.sql` and `V2__seed.sql`: three hospitals, one admin
(`admin@careride.com` / `Admin@123`), and 30-minute slots from 06:00 to 20:00
for the next 14 days with capacity 3. Hibernate uses `ddl-auto: validate`.
The migrations are PostgreSQL-specific; if the target database already has a
Flyway history from MySQL, use a fresh database or repair that history.

## Health check

`GET http://localhost:8080/actuator/health` is public and reports app/database status.
`GET http://localhost:8080/actuator/info` is public and reports basic app info.

## Run with Docker Compose

```bash
export JWT_SECRET=$(openssl rand -base64 48)
read -s -p "Neon DB password: " DB_PASSWORD; export DB_PASSWORD
./mvnw clean package
docker compose up --build
```

Compose starts the app on port `8080` and connects it to the configured Neon database.

## API examples

```bash
BASE=http://localhost:8080

# 1. Signup
curl -s -X POST "$BASE/api/auth/signup" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Rahul Verma","email":"rahul@example.com","phone":"9876543210","password":"Password@123"}'

# 2. Login
TOKEN=$(curl -s -X POST "$BASE/api/auth/login" \
  -H 'Content-Type: application/json' \
  -d '{"email":"rahul@example.com","password":"Password@123"}' | jq -r .token)

# 3. List hospitals
curl -s "$BASE/api/hospitals" -H "Authorization: Bearer $TOKEN"

# 4. List available slots for tomorrow
DATE=$(date -v+1d +%F)
curl -s "$BASE/api/slots?date=$DATE" -H "Authorization: Bearer $TOKEN"

# 5. One-way booking
curl -s -X POST "$BASE/api/bookings" \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"hospitalId":1,"patientName":"Asha Verma","patientPhone":"9876543211","homeAddress":"12 Main Street, Bengaluru","mobilityNeed":"WALKING","notes":"","rides":[{"direction":"HOME_TO_HOSPITAL","slotId":10}]}'

# 6. Round trip (return slot must start after the pickup slot ends)
curl -s -X POST "$BASE/api/bookings" \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"hospitalId":1,"patientName":"Asha Verma","patientPhone":"9876543211","homeAddress":"12 Main Street, Bengaluru","mobilityNeed":"WHEELCHAIR","notes":"Need wheelchair","rides":[{"direction":"HOME_TO_HOSPITAL","slotId":10},{"direction":"HOSPITAL_TO_HOME","slotId":16}]}'
```

Also see `api-requests.http`.

## Tests

```bash
./mvnw test
```

Unit tests use Mockito; the signup/login integration test uses in-memory H2 with Flyway disabled.
