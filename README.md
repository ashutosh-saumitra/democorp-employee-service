
# democorp-employee-service

A **learning-oriented Spring Boot microservice** to manage DemoCorp employee details. It exposes REST APIs, uses an in‑memory **H2** database, is **containerized with Docker**, built by **Jenkins**, and deployed locally on **Kubernetes (Minikube)** with **Ingress**.

---

## Contents

- #architecture
- #tech-stack
- #domain-model
- #project-structure
- #run-locally-no-dockerk8s
- #build-docker-image
- #jenkins-ci-pipeline
- #kubernetes-minikube-deployment
- #ingress-access-employeelocal
- #ports--networking-whats-going-on
- #common-commands-cheat-sheet
- #api-endpoints
- #health--h2-console
- #troubleshooting
- #next-steps
- #license

---

## Architecture

```
Browser/Postman → Ingress (nginx, port 80)
                → Service (ClusterIP, port 80 → targetPort 4040)
                → Pod (Spring Boot on port 4040)
                → H2 in-memory DB
```

- **Ingress host:** `employee.local`
- **App (internal) port:** `4040`
- **Service (cluster) port:** `80 → 4040`
- **External access:** via Ingress (`http://employee.local/...`)
- **Alternate access (dev):** port-forward

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
## Tech Stack

- **Language/Runtime:** Java 21, Spring Boot 3.x/4.x
- **Dependencies:** Spring Web, Spring Data JPA, H2, Validation, Lombok, Actuator
- **Build:** Maven (Maven Wrapper `mvnw`)
- **Container:** Docker (Temurin JDK/JRE 17 images in Dockerfile, runs any Java 17+ compiled artifact)
- **CI/CD:** Jenkins (Declarative Pipeline)
- **Kubernetes:** Minikube + nginx Ingress
- **Database:** H2 (in-memory)
- **Version Control:** GitHub

---

## Domain Model

**Employee**  
Fields:  
`id, employeeCode, firstName, lastName, email, phone, department, jobTitle, status, employmentType, managerId, location, dateOfJoining, dateOfExit, createdAt, updatedAt, version, notes`

- JPA annotations (`@Entity`, `@Id`, `@GeneratedValue`, `@Version`)
- Unique: `employeeCode`
- Useful indexes: `email` (optional), `department`, `status`, `managerId`

---

## Project Structure
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
 ├── k8s
 │   ├── namespace.yaml
 │   ├── deployment.yaml
 │   ├── services.yaml
 │   └── ingress.yaml
 ├── pom.xml
 ├── Dockerfile
 ├── .dockerignore
 ├── Jenkinsfile
 └── README.md
```

---

## Run Locally (no Docker/K8s)

```bash
# Windows
mvnw.cmd clean spring-boot:run

# Mac/Linux
./mvnw clean spring-boot:run
```

- App runs on **http://localhost:4040**
- Health: `http://localhost:4040/api/v1/health`
- H2 Console: `http://localhost:4040/h2-console`
    - JDBC URL: `jdbc:h2:mem:democorp`
    - User: `sa` (or `SA`)
    - Password: *(blank unless configured)*

---

## 🐳 Build Docker Image

```bash
# Build JAR via Maven Wrapper
# Windows
mvnw.cmd -B -DskipTests clean package

# Mac/Linux
./mvnw -B -DskipTests clean package

# Build image (from project root)
docker build -t democorp-employee-service:latest .
```

Run image (optional local test):

```bash
docker run --rm -p 4040:4040 democorp-employee-service:latest
# Open http://localhost:4040/api/v1/health
```

---

## 🔁 Jenkins CI Pipeline

**Jenkinsfile** stages:

- **Checkout** → **Build & Test** (Maven Wrapper) → **Package** (JAR) → **Docker Build**
- Optional: **Docker Push** (if you configure a registry & credentials)
- Windows-friendly using `bat` + `mvnw.cmd`

**Notes:**
- Jenkins **Agent** runs under your Windows user (so it can access Docker Desktop)
- Master stays `Local System` (cannot access Docker)
- Start agent with:
  ```
  java -jar agent.jar -url http://localhost:9090/ -secret <...> -name "windows-docker-agent" -webSocket -workDir "C:\jenkins-agent"
  ```

---

## ☸️ Kubernetes (Minikube) Deployment

**1) Start Minikube + enable Ingress**
```bash
minikube start --driver=docker
minikube addons enable ingress
```

**2) Load local image into Minikube**
```bash
minikube image load democorp-employee-service:latest
```

**3) Apply manifests**
```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/ingress.yaml
```

**4) Verify**
```bash
kubectl get pods -n democorp      # expect 1/1 Running
kubectl get svc -n democorp
kubectl get ingress -n democorp
kubectl get endpoints democorp-employee-service -n democorp
```

---

## Ingress Access (employee.local)

### Option A — **Clean (no port in URL)**
Forward **local port 80 → ingress 80** (requires Admin CMD; used in corp laptops):

```bat
@echo off
set KUBECONFIG=C:\Users\YOUR_USER\.kube\config
kubectl -n ingress-nginx port-forward svc/ingress-nginx-controller 80:80
```

Add to `hosts`:
```
127.0.0.1 employee.local
```

Now open:
```
http://employee.local/api/v1/health
http://employee.local/h2-console
```

### Option B — **Browser/Postman on 4040 (bypass Ingress)**
For development mirroring your `server.port=4040`:

```bat
set KUBECONFIG=C:\Users\YOUR_USER\.kube\config
kubectl -n democorp port-forward deploy/democorp-employee-service 4040:4040
# or: kubectl -n democorp port-forward svc/democorp-employee-service 4040:80
```

Now open:
```
http://localhost:4040/api/v1/health
http://localhost:4040/h2-console
```

> In corporate setup, **NodePort** (e.g., `192.168.49.2:30xxx`) often fails due to firewall restrictions. Prefer **port-forward** or **tunnel**.

---

## Ports & Networking: What’s Going On

- **Inside the pod:** app listens on **4040** (from `application.properties`)
- **K8s Service:** exposes **port 80** → routes to `targetPort 4040`
- **Ingress:** routes `employee.local` (port **80**) → Service (80) → Pod (4040)
- **Port-forward (80:80):** your local 80 → ingress 80 (so `employee.local` works)
- **Port-forward (4040:4040):** your local 4040 → pod 4040 (bypasses ingress; useful for dev)

**Public URLs (via Ingress):**
```
http://employee.local/api/v1/health
http://employee.local/api/v1/employees
http://employee.local/h2-console
```

**Alt dev URLs (via port-forward to 4040):**
```
http://localhost:4040/api/v1/health
http://localhost:4040/h2-console
```

---

## Common Commands (Cheat Sheet)

**Docker**
```bash
docker build -t democorp-employee-service:latest .
docker images
docker run --rm -p 4040:4040 democorp-employee-service:latest
docker ps
docker stop <container-id>
```

**Minikube**
```bash
minikube start --driver=docker
minikube status
minikube ip
minikube addons enable ingress
minikube image load democorp-employee-service:latest
minikube update-context
```

**Kubernetes (kubectl)**
```bash
# Apply manifests
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/ingress.yaml

# Rollouts
kubectl rollout restart deployment democorp-employee-service -n democorp
kubectl rollout status deployment democorp-employee-service -n democorp

# Inspect
kubectl get pods -n democorp
kubectl get pods -n democorp -w
kubectl get svc -n democorp
kubectl get ingress -n democorp
kubectl get endpoints democorp-employee-service -n democorp

# Logs / Debug
kubectl logs -n democorp <pod>
kubectl describe pod -n democorp <pod>
kubectl exec -n democorp -it <pod> -- sh
kubectl exec -n democorp -it <pod> -- sh -lc "ss -ltnp || netstat -ltnp"
kubectl exec -n democorp -it <pod> -- sh -lc "wget -qO- http://127.0.0.1:4040/api/v1/health || echo fail"

# Port-forward patterns
kubectl -n ingress-nginx port-forward svc/ingress-nginx-controller 80:80
kubectl -n democorp port-forward svc/democorp-employee-service 4040:80
kubectl -n democorp port-forward deploy/democorp-employee-service 4040:4040
```

**kubectl Context (corp laptops)**
```bash
kubectl config get-contexts
kubectl config use-context minikube
kubectl config current-context
set KUBECONFIG=C:\Users\YOUR_USER\.kube\config
kubectl cluster-info
```

**Windows hosts**
```text
127.0.0.1 employee.local
```

---

## API Endpoints

**Base Path:** `/api/v1`

- `GET /api/v1/health` → `"OK-democorp-employee-service"`
- `POST /api/v1/employees` → create
- `GET /api/v1/employees/{id}` → get by id
- `GET /api/v1/employees/code/{employeeCode}` → get by code
- `PUT /api/v1/employees/{id}` → update
- `DELETE /api/v1/employees/{id}` → delete
- `GET /api/v1/employees` → list with pagination/sorting/filtering:
    - Query params: `page`, `size`, `sort`, `department`, `status`, `employmentType`, `managerId`, `q`
    - POST /api/v1/employees/search?page=0&size=10&sort=firstName,asc

**DTOs & Validation**
- Create/Update/Response DTOs
- Bean Validation (`@NotBlank`, `@Email`, etc.)
- Global exception handler returns structured JSON error

---

## Health & H2 Console

- **Health:**
    - Public (via Ingress): `http://employee.local/api/v1/health`
    - Dev (port-forward): `http://localhost:4040/api/v1/health`

- **Actuator:** (exposed minimally)
    - `management.endpoint.health.probes.enabled=true`
    - `management.endpoints.web.exposure.include=health,info`

- **H2 Console:**
    - Public (via Ingress): `http://employee.local/h2-console`
    - Dev (port-forward): `http://localhost:4040/h2-console`
    - JDBC URL: `jdbc:h2:mem:democorp`

**Note on Probes:**
- For stable startup on K8s, probes target **`/api/v1/health`** with sensible `timeoutSeconds` and `failureThreshold`.
- Liveness probe can be omitted initially (startup + readiness are often sufficient for Spring Boot).

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
## Troubleshooting

- **Ingress not reachable on corp laptop:**
    - Use ingress port-forward: `kubectl -n ingress-nginx port-forward svc/ingress-nginx-controller 80:80`
    - Ensure `127.0.0.1 employee.local` in hosts
    - Test: `curl http://employee.local/api/v1/health`

- **NodePort blocked:**
    - Corporate firewalls usually block `192.168.49.2:30xxx`. Prefer **port-forward** or **tunnel**.

- **kubectl pointing to `localhost:8080`:**
    - In Admin CMD, set: `set KUBECONFIG=C:\Users\YOUR_USER\.kube\config`
    - `kubectl config use-context minikube`

- **Pods stuck `0/1 Ready`:**
    - Check `kubectl describe pod ...` for probe failures
    - Use custom health endpoint `/api/v1/health` for probes with `timeoutSeconds: 5`
    - Avoid aggressive liveness during bring-up; keep startup + readiness

- **Image updates not reflected:**
    - Rebuild → `minikube image load democorp-employee-service:latest` → `kubectl rollout restart ...`

---

## Next Steps

- Add **ConfigMap/Secret** for environment-based config
- Add **resource requests/limits** and optionally **HPA**
- Add **SonarQube** & **coverage** to Jenkins
- Add **GitHub webhook** → auto-build on push
- Package **Helm chart** for easier deployment
- Add Swagger/OpenAPI documentation 
- Add JWT-based security
- Add central logging (ELK stack)
- Add Grafana + Prometheus monitoring
- Implement Blue/Green or Canary deployment

---

## License

This project is for learning and demonstration purposes. Use freely within your organization’s policy.

---

### Quick Recap (Demo-Ready)

- App runs on **4040** inside pod
- Exposed as **Service:80** → **Ingress:80**
- On corp laptop, use **port-forward 80:80** → `http://employee.local/...` (clean)
- Alternative dev mode: **port-forward 4040:4040** → `http://localhost:4040/...`
- Rebuild+reload image: `docker build` → `minikube image load` → `rollout restart`
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