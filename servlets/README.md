
# User Access Management System - Servlets Package

This `servlets` package is part of the **User Access Management System** Maven project, which is designed to handle user access requests for software applications within an organization. This project includes different roles: **Employee**, **Manager**, and **Admin**.

The package contains Java Servlets that handle the core functionality of the system, including user login, software creation, access requests, and request approvals.

## Project Structure

### Servlets in this package:
1. **LoginServlet.java** - Handles user login based on roles (Employee, Manager, Admin) and redirects users to appropriate pages.
2. **SignUpServlet.java** - Allows new users to sign up and creates accounts with the default role of "Employee".
3. **RequestServlet.java** - Enables employees to submit access requests for software.
4. **ApprovalServlet.java** - Allows managers to approve or reject access requests.
5. **SoftwareServlet.java** - Allows admins to add new software to the system.
6. **SoftwareListServlet.java** - Fetches and displays the list of software for access request selection.
7. **LogoutServlet.java** -  Handles user logout.
