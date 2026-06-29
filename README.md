# Smart Employee Management System (SEMS)

A complete, industry-grade Employee Management System built with **Spring Boot 3**, **Spring Security (JWT)**, **Spring Data JPA**, **MySQL**, and a modern **HTML + CSS + Bootstrap 5 + Vanilla JS** frontend.

> Follows MVC architecture, clean code, layered structure (Entity → Repository → Service → Controller), centralized exception handling, validation, role-based access (ADMIN / EMPLOYEE), PDF & Excel exports, file uploads, and dashboard analytics with Chart.js.

---

## 1. Features

| Module | Features |
|---|---|
| **Authentication** | JWT login, logout, register, role-based access (ADMIN, EMPLOYEE) |
| **Employees** | CRUD, search, pagination, sorting |
| **Departments** | CRUD (admin) |
| **Leaves** | Apply leave, admin approve/reject, leave history |
| **Profile** | Update profile, change password, upload profile picture |
| **Dashboard** | Total/Active/Inactive employees, department breakdown, charts |
| **Reports** | Export employee list as PDF (iText) and Excel (Apache POI) |
| **Cross-cutting** | Validation, global exception handler, logging, BCrypt, CORS, Swagger UI |

---

## 2. Tech Stack

- **Backend**: Java 17, Spring Boot 3.3, Spring Security, Spring Data JPA, Hibernate, JJWT 0.12, iText 5, Apache POI 5
- **Database**: MySQL 8
- **Frontend**: HTML5, CSS3, Bootstrap 5.3, Vanilla JS, Chart.js 4
- **Tools**: Maven, IntelliJ IDEA, Git/GitHub, Swagger / springdoc-openapi

---

## 3. Project Structure

```
smart-employee-management-system/
├── pom.xml
├── README.md
├── sql/schema.sql
├── uploads/                        # profile pictures (runtime)
└── src/main/
    ├── java/com/sems/
    │   ├── SmartEmsApplication.java
    │   ├── config/
    │   │   ├── SecurityConfig.java
    │   │   └── DataInitializer.java
    │   ├── security/
    │   │   ├── JwtUtil.java
    │   │   ├── JwtAuthFilter.java
    │   │   └── CustomUserDetailsService.java
    │   ├── entity/
    │   │   ├── User.java   Role.java
    │   │   ├── Employee.java
    │   │   ├── Department.java
    │   │   └── LeaveRequest.java
    │   ├── repository/
    │   │   ├── UserRepository.java
    │   │   ├── EmployeeRepository.java
    │   │   ├── DepartmentRepository.java
    │   │   └── LeaveRepository.java
    │   ├── dto/
    │   │   ├── LoginRequest.java  RegisterRequest.java  AuthResponse.java
    │   │   ├── EmployeeDto.java  DepartmentDto.java
    │   │   ├── LeaveDto.java     ChangePasswordRequest.java
    │   ├── service/
    │   │   ├── AuthService.java
    │   │   ├── EmployeeService.java
    │   │   ├── DepartmentService.java
    │   │   ├── LeaveService.java
    │   │   ├── ProfileService.java
    │   │   ├── DashboardService.java
    │   │   └── ReportService.java
    │   ├── controller/
    │   │   ├── AuthController.java
    │   │   ├── EmployeeController.java
    │   │   ├── DepartmentController.java
    │   │   ├── LeaveController.java
    │   │   ├── ProfileController.java
    │   │   ├── DashboardController.java
    │   │   └── ReportController.java
    │   └── exception/
    │       ├── GlobalExceptionHandler.java
    │       ├── ResourceNotFoundException.java
    │       └── ErrorResponse.java
    └── resources/
        ├── application.properties
        └── static/
            ├── css/style.css
            ├── js/app.js
            ├── index.html  login.html
            ├── dashboard.html  employees.html
            ├── departments.html  leaves.html
            ├── profile.html  reports.html
```

---

## 4. Database Schema (MySQL)

- `users(id, username, email, password, role, enabled)`
- `departments(id, name, description)`
- `employees(id, user_id, first_name, last_name, email, phone, job_title, salary, status, hire_date, profile_picture, department_id)`
- `leaves(id, employee_id, leave_type, start_date, end_date, reason, status, applied_at)`

JPA `ddl-auto=update` will create tables automatically. You can also run `sql/schema.sql` manually.

---

## 5. Setup (Step-by-Step)

### Prerequisites
- JDK 17+
- Maven 3.9+
- MySQL 8 running on `localhost:3306`
- IntelliJ IDEA (Community or Ultimate)

### Steps

1. **Clone & open**
   ```bash
   git clone <your-fork-url> smart-employee-management-system
   cd smart-employee-management-system
   ```
   Open the folder in IntelliJ → it will detect the `pom.xml` and import Maven dependencies.

2. **Create database (optional, JPA can auto-create)**
   ```bash
   mysql -uroot -p < sql/schema.sql
   ```

3. **Configure DB credentials** in `src/main/resources/application.properties`:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/sems_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=root
   ```

4. **Run**
   - In IntelliJ: run `SmartEmsApplication`
   - Or via CLI:
     ```bash
     mvn spring-boot:run
     ```

5. **Open the app**
   - UI:      <http://localhost:8080/login.html>
   - Swagger: <http://localhost:8080/swagger-ui.html>

6. **Default users (seeded automatically on first run)**

   | Role     | Username  | Password      |
   |----------|-----------|---------------|
   | ADMIN    | admin     | Admin@123     |
   | EMPLOYEE | employee  | Employee@123  |

---

## 6. API Documentation (high level)

All protected endpoints require `Authorization: Bearer <JWT>` header.

### Auth
| Method | URL | Body | Auth |
|---|---|---|---|
| POST | `/api/auth/login` | `{username,password}` | public |
| POST | `/api/auth/register` | `{username,email,password,firstName,lastName,role?}` | public |
| POST | `/api/auth/logout` | – | any |

### Employees
| Method | URL | Auth |
|---|---|---|
| GET    | `/api/employees?q=&page=0&size=10&sortBy=id&dir=asc` | any |
| GET    | `/api/employees/{id}` | any |
| POST   | `/api/employees` | ADMIN |
| PUT    | `/api/employees/{id}` | ADMIN |
| DELETE | `/api/employees/{id}` | ADMIN |

### Departments
`GET/POST/PUT/DELETE /api/departments[/{id}]` — write ops require ADMIN.

### Leaves
| Method | URL | Auth |
|---|---|---|
| GET    | `/api/leaves` | ADMIN |
| GET    | `/api/leaves/employee/{employeeId}` | any |
| POST   | `/api/leaves/employee/{employeeId}` | any |
| PATCH  | `/api/leaves/{id}/status?status=APPROVED\|REJECTED` | ADMIN |

### Profile
| Method | URL |
|---|---|
| GET    | `/api/profile` |
| PUT    | `/api/profile` |
| POST   | `/api/profile/change-password` |
| POST   | `/api/profile/picture` (multipart) |

### Dashboard / Reports
- `GET /api/dashboard/stats`
- `GET /api/reports/employees/pdf`
- `GET /api/reports/employees/excel`

Full interactive docs: `/swagger-ui.html`.

---

## 7. Security Notes

- Passwords hashed with **BCrypt**.
- Stateless authentication using **JWT** (HS256, 24h expiry by default).
- Role-based access via `@PreAuthorize` and `SecurityFilterChain` rules.
- Configurable JWT secret in `application.properties` (`app.jwt.secret`). **Replace it in production.**

---

## 8. Build a runnable JAR

```bash
mvn clean package -DskipTests
java -jar target/smart-employee-management-system-1.0.0.jar
```

---

## 9. License

MIT — use freely for learning, portfolio, or production.
