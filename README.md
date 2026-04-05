# Finance Dashboard Backend

Spring Boot backend for finance data processing with role-based access control.

## Tech Stack
- Java 17
- Spring Boot 3
- Spring Web, Spring Data JPA, Bean Validation
- H2 in-memory database

## Features Implemented
- User and role management (VIEWER, ANALYST, ADMIN)
- User status management (`active` / `inactive`)
- Financial records CRUD
- Record filtering by type, category, and date range
- Dashboard APIs: summary, category-wise totals, monthly trends, recent activity
- Backend access control enforcement by role
- Input validation and global error handling
- Data persistence with JPA + H2

## RBAC Rules
- `VIEWER`: dashboard endpoints only
- `ANALYST`: dashboard + read/filter financial records
- `ADMIN`: full access to users and financial records

## Mock Authentication
This project uses a simple header-based mock auth for assignment evaluation.

Pass user ID in every protected request:

```http
X-User-Id: <user-id>
```

Seeded users (auto-created on startup):
- `admin@finance.local` (ADMIN)
- `analyst@finance.local` (ANALYST)
- `viewer@finance.local` (VIEWER)
On a fresh in-memory startup, their IDs are typically `1`, `2`, `3` respectively.

You can also bootstrap first admin on an empty DB via:
- `POST /api/users/bootstrap-admin`

## Run Locally
1. Ensure Java 17+ and Maven 3.8+
2. From project root:

```bash
mvn spring-boot:run
```

3. Server runs at `http://localhost:8080`

## API Endpoints

### Users (ADMIN only)
- `GET /api/users`
- `GET /api/users/{id}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`
- `POST /api/users/bootstrap-admin` (no header required, only when user table is empty)

### Financial Records
- `GET /api/records` (ANALYST, ADMIN)
- `GET /api/records/filter?type=INCOME&category=Salary&fromDate=2026-01-01&toDate=2026-03-31` (ANALYST, ADMIN)
- `POST /api/records` (ADMIN)
- `PUT /api/records/{id}` (ADMIN)
- `DELETE /api/records/{id}` (ADMIN)

### Dashboard (VIEWER, ANALYST, ADMIN)
- `GET /api/dashboard/summary`
- `GET /api/dashboard/category-totals`
- `GET /api/dashboard/monthly-trends`
- `GET /api/dashboard/recent-activity?limit=5`

## Sample Summary Response
```json
{
  "totalIncome": 5000.00,
  "totalExpenses": 1200.00,
  "netBalance": 3800.00
}
```

## Notes and Trade-offs
- Authentication is intentionally mocked using `X-User-Id` for assignment simplicity.
- H2 in-memory DB is used for easy local setup.
- For production, replace mock auth with JWT/session auth and use a persistent database.
