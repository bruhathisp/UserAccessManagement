# User Access Management System (Leucine Project)

## Overview

The **User Access Management System** is a web-based platform designed to streamline and automate user access requests within an organization. It leverages **role-based access control (RBAC)**, offering distinct functionalities for three types of users:

- **Employee**: Can request access to various software applications.
- **Manager**: Can view, approve, or reject access requests submitted by employees.
- **Admin**: Has full control to manage software applications and user roles, ensuring secure access management across the system.

This project utilizes **Java Servlets**, **JSPs**, and **PostgreSQL** to handle backend processing, data storage, and user authentication. Deployed with **Apache Tomcat** as a web server and managed via **Maven**, it ensures a robust, scalable, and secure environment to handle user access efficiently.

---

## Project Architecture

### Key Technologies:
- **Backend**: Java Servlets & JSP (JavaServer Pages)
- **Database**: PostgreSQL (used for storing users, software, and access requests)
- **Build Tool**: Maven (for project management and build automation)
- **Server**: Apache Tomcat (for deploying the web application)

### Roles:
- **Employee**: Can submit access requests for software.
- **Manager**: Approves or rejects software access requests.
- **Admin**: Manages users, software, and all access-related functionality.

---

## Prerequisites

Before running the project, ensure that you have the following installed and configured:

- **Java 17 or later**
- **Maven** (to build and run the project)
- **PostgreSQL** (configured with required tables and default users)

---

## Dependencies Breakdown

1. **Jakarta EE API**: This is the Jakarta platform API that includes all the specifications for web applications (like Servlets, JSP, etc.).
   - **Version**: `10.0.0`
   - **Scope**: `provided` because Tomcat will provide the runtime environment for the application.

2. **PostgreSQL JDBC Driver**: This is the JDBC driver required to connect your Java application to a PostgreSQL database.
   - **Version**: `42.3.1` (or the latest stable version)

3. **Servlet API**: Although the Jakarta EE API should cover Servlets, some configurations or Tomcat setups might require the `javax.servlet-api` dependency.
   - **Version**: `4.0.1`
   - **Scope**: `provided` (same reason as above).

---


## Database Setup

### Required Tables

Ensure the following tables exist in your PostgreSQL database. You can find the SQL script to create these tables in the `sql` directory of the project.

```sql
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('Employee', 'Manager', 'Admin'))
);

CREATE TABLE IF NOT EXISTS software (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    description TEXT,
    access_levels TEXT CHECK (access_levels IN ('Read', 'Write', 'Admin'))
);

CREATE TABLE IF NOT EXISTS requests (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    software_id INTEGER REFERENCES software(id) ON DELETE CASCADE,
    access_type TEXT NOT NULL CHECK (access_type IN ('Read', 'Write', 'Admin')),
    reason TEXT,
    status TEXT DEFAULT 'Pending' CHECK (status IN ('Pending', 'Approved', 'Rejected'))
);

-- Default Manager and Admin Users
INSERT INTO users (username, password, role) VALUES
('manager', 'manager_password', 'Manager'),
('admin', 'admin_password', 'Admin');
```

> **Note:** Remember to replace `'manager_password'` and `'admin_password'` with actual secure passwords.

---

## Running the Project

Follow these steps to set up and run the project:

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/User-Access-Management-System.git
cd User-Access-Management-System
```

### 2. Configure Database Connection

Make sure to update the database connection details in the servlets. Example database connection:

```java
String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres";
String dbUser = "postgres";
String dbPassword = "password";
```

### 3. Build the Project

Use **Maven** to compile and package the project:

```bash
mvn clean package
```

### 4. Deploy to Tomcat

- Copy the generated `.war` file (located in the `target` directory) to the **Tomcat webapps** folder.
- Start or restart Tomcat.

### 5. Access the Application

Open a web browser and navigate to:

```
http://localhost:8080/mavenproject1
```

Login with the following credentials:

- **Manager**: username = `manager`, password = `manager_password`
- **Admin**: username = `admin`, password = `admin_password`

---

## Features

- **Login**: Users log in based on their roles (Employee, Manager, Admin).
- **Sign Up**: New users can sign up with the default "Employee" role.
- **Request Access**: Employees can request access to software.
- **Approve/Reject Requests**: Managers can manage software access requests.
- **Create Software**: Admins can add new software applications to the system.

---

## Project Structure

```plaintext
UserAccessManagement/
├── src/
│   └── servlets/
│       ├── SignUpServlet.java
│       ├── LoginServlet.java
│       ├── SoftwareServlet.java
│       ├── RequestServlet.java
│       └── ApprovalServlet.java
├── webapp/
│   ├── signup.jsp
│   ├── login.jsp
│   ├── createSoftware.jsp
│   ├── requestAccess.jsp
│   └── pendingRequests.jsp
├── WEB-INF/
│   └── web.xml
└── pom.xml
```

---

## Troubleshooting

- **Database Connection Errors**: Ensure that the PostgreSQL database is running and credentials are correct.
- **Class Not Found Errors**: Verify that the necessary dependencies are correctly specified in the `pom.xml` file. Run `mvn clean package` again to ensure the dependencies are correctly resolved.
- **Deployment Issues**: Make sure to restart Tomcat after deploying the `.war` file.

---


## Contact

For any issues or contributions, feel free to open an issue or submit a pull request on GitHub.

---

## Acknowledgements

- **Apache Tomcat** for serving the application.
- **PostgreSQL** for reliable database management.
- **NetBeans IDE** for development.

--- 

