# TaskManagementSoftware
Domain driven design based Software for task management.

Task Management System (API)
A simplified backend for a Task Management System built with Spring Boot, following Domain-Driven Design (DDD) principles and Clean Code practices.

🚀 Features
Full CRUD: Create, Read, Update, and Delete tasks.
DDD Architecture: Clear separation between Controller, Service, Repository, and Domain layers.
Global Error Handling: Consistent JSON error responses for 404 (Not Found) and 400 (Bad Request).
Advanced Filtering: List tasks with pagination and status filtering (PENDING, IN_PROGRESS, DONE).
Validation: Mandatory fields (title, due_date) and future-date validation.

🛠️ Prerequisites
Before you begin, ensure you have the following installed:
Java JDK 17 or higher.
Maven 3.6+ (for dependency management).
IDE (IntelliJ IDEA, Eclipse, or VS Code).
Lombok Plugin: Ensure your IDE has the Lombok plugin installed and "Annotation Processing" is enabled.

📦 Installation & Setup
Clone the project (or unzip the files into a folder).
Install Dependencies: Open your terminal in the project root and run:
Bash: mvn clean install

Run the Application:
mvn spring-boot:run

🏗️ Architecture (DDD)
Controller: Handles HTTP routing and Request/Response mapping.
Service: Orchestrates business logic and validations.
Repository: Abstract interface for data persistence.
Model: Domain entities and Enums.
Exception: Centralized global error handling.

API Endpoints:

1. Create a Task
URL: POST /api/v1/tasks

Body:

JSON
{
    "title": "Build Spring App",
    "description": "Implement DDD and TDD",
    "status": "PENDING",
    "dueDate": "2026-12-31"
}

2. Get Task by ID
URL: GET /api/v1/tasks/{id}

Example: GET /api/v1/tasks/550e8400-e29b-41d4-a716-446655440000

3. List All Tasks (With Filtering & Pagination)
URL: GET /api/v1/tasks

Optional Query Parameters:

status: Filter by PENDING, IN_PROGRESS, or DONE.
page: Page number (starts at 0).
size: Number of tasks per page.

Example URL:
http://localhost:8080/api/v1/tasks?status=PENDING&page=0&size=5

4. Update a Task
URL: PUT /api/v1/tasks/{id}

Body: (Send only the fields you want to change)

JSON
{
    "status": "IN_PROGRESS",
    "description": "Updated description"
}
5. Delete a Task
URL: DELETE /api/v1/tasks/{id}

Response: 204 No Content
Note: Make sure you actually enable Annotation Processing in your IDE (IntelliJ or Eclipse). Because we used Lombok, the code won't compile unless your IDE is told to look for those @Data and @RequiredArgsConstructor tags!


