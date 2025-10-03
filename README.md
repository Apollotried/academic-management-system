# Academic Management System (Microservices)

![Java](https://img.shields.io/badge/Java-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-green)
![React](https://img.shields.io/badge/React-%2361DAFB)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-%23316192)
![Kafka](https://img.shields.io/badge/Apache_Kafka-%23231F20)
![Docker](https://img.shields.io/badge/Docker-%230db7ed)

A comprehensive **full-stack microservices platform** for academic institution management, featuring interconnected services with event-driven architecture, role-based access control, and real-time notifications.

## 🚀 Key Features

### 🔐 Authentication & Security
- **JWT-based authentication** with role-based access control
- **Secure token validation** across all microservices
- **User registration** with different roles (Admin, Student, Teacher)
- **API Gateway security** with request filtering

### 👨‍🎓 Student Management
- Complete student CRUD operations
- Student profile management
- Enrollment tracking and management

### 📚 Course Management
- Comprehensive course catalog
- Syllabus creation and assignment system
- Teacher-course association
- Real-time course notifications

### 👨‍🏫 Professor Management
- Faculty member management
- Teacher profiles and assignments
- Department management

### 📝 Enrollment System
- Course enrollment and registration
- Student-course association
- Cross-service validation

### 📊 Results Management
- Grade entry and management
- Batch grade processing
- Student performance tracking
- Real-time grade notifications

### 🔔 Real-time Notifications
- **Kafka-based event streaming**
- **Server-Sent Events (SSE)** for real-time updates
- Grade and course notifications
## 🛠️ Tech Stack

### 🔄 Service Communication
- **Synchronous communication** via Spring Cloud OpenFeign
- **Asynchronous communication** via Apache Kafka
- **Service discovery** with Eureka
- **Load balanced** API calls between services

| Category       | Technologies Used |
|---------------|------------------|
| **Backend**   | Java 21, Spring Boot 3, Spring Cloud, JWT |
| **Database**  | PostgreSQL, Hibernate, JPA |
| **Messaging** | Apache Kafka, Zookeeper |
| **Frontend**  | React, HTML5, CSS3 |
| **Infrastructure** | Docker, Docker Compose |
| **Service Discovery** | Eureka Server |
| **API Gateway** | Spring Cloud Gateway |
| **Service Communication** | Spring Cloud OpenFeign |
| **Configuration** | Spring Cloud Config |


## 🐳 Dockerized Services

### Infrastructure Services
- **PostgreSQL**: Primary database (port 5432)
- **PgAdmin**: Database management UI (port 5050)
- **Zookeeper**: Kafka coordination (port 2181)
- **Kafka**: Event streaming platform (port 9092)

### Core Microservices
- **Discovery Service**: Service registry (port 8761)
- **Config Service**: Centralized configuration (port 9999)
- **Gateway Service**: API Gateway (port 8888)
- **Auth Service**: Authentication (port 8090)
- **Etudiant Service**: Student management (port 8080)
- **Cours Service**: Course management (port 8081)
- **Prof Service**: Professor management (port 8083)
- **Inscription Service**: Enrollment system (port 8084)
- **Resultat Service**: Grades management (port 8082)
- **Notification Service**: Real-time notifications (port 8099)



Crafted with ❤️ using Spring Boot Microservices & Docker Containers
