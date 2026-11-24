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
- MySQL 8, or Docker with Compose

## Environment variables

| Variable | Required | Example |
|---|---|---|
| `JWT_SECRET` | production yes, dev optional | `openssl rand -base64 48` (must decode to at least 32 bytes) |
| `JWT_EXPIRATION_MS` | no | `3600000` |
| `DB_URL` | no | `jdbc:mysql://localhost:3306/care_ride?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` |
| `DB_USERNAME` | no | `root` |
| `DB_PASSWORD` | no | `mypassword` |

If `JWT_SECRET` is not set, the app logs a warning and uses an ephemeral key: it starts, but tokens stop working after a restart. Always set it in production. In IntelliJ, add `JWT_SECRET` under Run → Edit Configurations → Environment variables.

A running MySQL 8 instance is still required; use `docker compose up -d mysql` if needed.

## Run locally

```bash
export JWT_SECRET=$(openssl rand -base64 48)
export DB_USERNAME=root
export DB_PASSWORD=mypassword

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

## Run with Docker Compose

```bash
export JWT_SECRET=$(openssl rand -base64 48)
./mvnw clean package
docker compose up --build
```

Compose starts MySQL 8 and the app on port `8080`.

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
