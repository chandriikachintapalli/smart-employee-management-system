# Smart Employee Management System

## Overview

Smart Employee Management System is a full-stack web application developed using Java Spring Boot, Spring Security, JWT Authentication, MySQL, HTML, CSS, JavaScript, and Bootstrap. It provides a secure platform for managing employees, departments, leave requests, user profiles, and administrative operations through a responsive web interface.

---

## Features

- Secure JWT-based Authentication and Authorization
- Role-Based Access Control
- Employee Management (Create, Read, Update, Delete)
- Department Management
- Leave Management
- User Profile Management
- Dashboard with Charts and Statistics
- Reports Module
- RESTful APIs
- Responsive User Interface

---

## Technology Stack

### Backend

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT Authentication
- Maven

### Frontend

- HTML
- CSS
- JavaScript
- Bootstrap

### Database

- MySQL

### Development Tools

- IntelliJ IDEA
- MySQL Workbench
- Git
- GitHub

---

## Project Structure

```text
smart-employee-management-system
│
├── sql/
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │   └── static/
│
├── uploads/
├── pom.xml
├── README.md
└── .gitignore
```

---

## Installation

### Clone the Repository

```bash
git clone https://github.com/chandriikachintapalli/smart-employee-management-system.git
```

### Open the Project

Import the project into IntelliJ IDEA as a Maven project.

### Configure the Database

Create a MySQL database and import the SQL script available in the `sql` folder.

Update the database credentials in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### Run the Application

Start the Spring Boot application.

Open:

```text
http://localhost:8080/login.html
```

---

## Future Improvements

- Attendance Management
- Payroll Management
- Email Notifications
- Employee Photo Upload
- Advanced Dashboard Analytics
- PDF and Excel Report Generation

---

## Author

**Jyothi Chandrika Chintapalli**

Email: chandrikachintapalli5@gmail.com

GitHub: https://github.com/chandriikachintapalli
