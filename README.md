\# Finance Dashboard Backend



A Spring Boot REST API for financial management with user roles and transaction tracking.



\## Features

\- User Management (CRUD with roles: VIEWER, ANALYST, ADMIN)

\- Financial Records (Income/Expense tracking)

\- Dashboard Summary (Total income, expenses, net balance)

\- Role-based access control

\- Data validation and error handling

\- H2 in-memory database



\## How to Run

1\. Prerequisites: Java 17+ and Maven 3.8+

2\. Navigate to project folder

3\. Run: `mvn spring-boot:run`

4\. Server starts at: \[localhost](http://localhost:8080)



\## API Endpoints



\### Users

\- GET `/api/users` - List all users

\- POST `/api/users` - Create user

\- PUT `/api/users/{id}` - Update user

\- DELETE `/api/users/{id}` - Delete user



\### Financial Records

\- GET `/api/records` - List all records

\- POST `/api/records` - Create record

\- PUT `/api/records/{id}` - Update record

\- DELETE `/api/records/{id}` - Delete record



\### Dashboard

\- GET `/api/dashboard/summary` - Get financial summary



\## Sample Data

```json

{

&#x20; "totalIncome": 5000.00,

&#x20; "totalExpenses": 1200.00,

&#x20; "netBalance": 3800.00

}



