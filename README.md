# Democorp Employee Service

A learning-oriented **Spring Boot microservice** that manages employee details for a fictional organization named **DemoCorp**.  
This project demonstrates end-to-end microservice development including API design, persistence layer, validation, DTO mapping, pagination, filtering, Dockerization, CI/CD, and Kubernetes deployment.

---

## 🚀 Features

- RESTful CRUD APIs for Employee resource
- H2 in-memory database for development/testing
- DTO-based API contract with clean entity separation
- Validation & standardized global exception handling
- Pagination, sorting, dynamic search filtering
- Dockerized for container-based deployment
- Jenkins pipeline for CI/CD
- Kubernetes manifests for local cluster deployment (Minikube/Kind)

---

## 🧱 Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Web / Spring Data JPA**
- **H2 Database**
- **Maven**
- **Docker**
- **Jenkins CI/CD**
- **Kubernetes (Minikube / Kind)**
- **GitHub for version control**

---

## 📂 Project Structure

```
democorp-employee-service
 ├── src/main/java/com/democorp/employee
 │   ├── controller
 │   ├── service
 │   ├── repository
 │   ├── entity
 │   ├── dto
 │   ├── exception
 │   └── mapper
 ├── src/main/resources
 │   ├── application.properties
 │   └── data.sql (optional)
 ├── pom.xml
 ├── Dockerfile
 └── README.md
```

---

## 🧪 API Endpoints

All APIs are versioned under:

```
/api/v1/employees
```

### 🔹 Health Check
```
GET /health
```

### 🔹 Create Employee
```
POST /api/v1/employees
```

### 🔹 Get Employee by ID
```
GET /api/v1/employees/{id}
```

### 🔹 Update Employee
```
PUT /api/v1/employees/{id}
```

### 🔹 Delete Employee
```
DELETE /api/v1/employees/{id}
```

### 🔹 Search / Filter / Pagination
```
POST /api/v1/employees/search?page=0&size=10&sort=firstName,asc
```

---

## 🗄️ Employee Entity Fields

| Field | Description |
|-------|-------------|
| id | Primary key |
| employeeCode | Unique employee code |
| firstName / lastName | Employee name |
| email | Email address |
| phone | Contact number |
| department | Department name |
| jobTitle | Job title |
| status | Active/Inactive/Terminated |
| employmentType | Full-time / Contract |
| managerId | Reporting manager |
| location | Work location |
| dateOfJoining | Start date |
| dateOfExit | End date |
| createdAt / updatedAt | Timestamps |
| version | Optimistic locking |
| notes | Additional info |

---

## 🛠️ Running the Project (Local)

### 1. Build and run using Maven
```
mvn clean install
mvn spring-boot:run
```

### 2. H2 Console
```
http://localhost:8080/h2-console
```
JDBC URL:
```
jdbc:h2:mem:testdb
```

---

## 🐳 Docker Support

### Build Docker image:
```
docker build -t democorp/employee-service:latest .
```

### Run the container:
```
docker run -p 8080:8080 democorp/employee-service:latest
```

---

## 🔁 CI/CD — Jenkins Pipeline

The Jenkins pipeline performs:

- Checkout source code
- Maven build + unit tests
- Docker image build
- (optional) Image push to registry
- (optional) Deploy to Kubernetes

Pipeline definition lives in `Jenkinsfile`.

---

## ☸️ Kubernetes Deployment

Located under `/k8s`:

- `deployment.yaml`
- `service.yaml`
- `ingress.yaml`

### Deploy the service:
```
kubectl apply -f k8s/
```

### Verify:
```
kubectl get pods
kubectl get svc
kubectl get ingress
```

---

## 🗺️ Future Improvements

- Add Swagger/OpenAPI documentation
- Add JWT-based security
- Add central logging (ELK stack)
- Add Grafana + Prometheus monitoring
- Implement Blue/Green or Canary deployment

---

## 👨‍💻 Author

**Ashutosh Saumitra**  
Senior Engineer — Technology  
📍 Delhi, India

---

## ⭐ Support & Contributions

This repository is part of a personal learning journey.  
Suggestions and PRs are welcome!

---
