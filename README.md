# 🔐 Auth Service

The **Auth Service** is a centralized authentication and authorization microservice responsible for handling **user authentication**, **token management**, and **security event publishing**.  
It acts as the **single source of truth for user identity** in the system and communicates with other microservices using an **event-driven architecture**.

This service is built using **:contentReference[oaicite:0]{index=0}**, **:contentReference[oaicite:1]{index=1}**, and **:contentReference[oaicite:2]{index=2}**.

---

## ✨ Features

- User **Signup (Registration)**
- User **Login (Authentication)**
- **JWT Access Token** generation
- **Refresh Token** issuance and rotation
- **Spring Security** integration
- Publishes **security events** to Kafka
- Consumed by other microservices for user context syncing

---

## 🎯 Responsibilities

This service is responsible for:

- Authenticating users
- Issuing and validating JWT tokens
- Managing refresh tokens
- Enforcing security policies
- Publishing authentication-related events

This service **does not handle business logic** such as expenses, budgets, or analytics.

---

## 🔁 Authentication Flow

1. User sends login or signup request
2. Credentials are validated
3. Access and refresh tokens are generated
4. A security event is published to Kafka
5. Other services consume the event

---

## 🧩 Tech Stack

- **Language**: Java  
- **Framework**: :contentReference[oaicite:3]{index=3}  
- **Security**: :contentReference[oaicite:4]{index=4} + JWT  
- **Messaging**: :contentReference[oaicite:5]{index=5}  
- **Database**: MySQL / PostgreSQL  
- **Build Tool**: Maven / Gradle  



