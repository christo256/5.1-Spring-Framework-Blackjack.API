## 📄 Blackjack API — Project Overview

This project is a **Reactive Blackjack Game API** built with **Spring Boot**, **Spring WebFlux**, **Reactive MongoDB**, and **MySQL**.

Players can start new games, perform moves (Hit or Stand), and automatically update their ranking based on game results.

The backend demonstrates a hybrid persistence architecture using two databases:

- **MongoDB (Reactive)** → Stores game state, cards, and Blackjack logic
- **MySQL (JPA)** → Stores player identity, statistics, and ranking

Players are created automatically when starting a new game using a `findOrCreate()` strategy.

The application is fully **Dockerized**, allowing complete deployment using Docker Compose.

---

## 🚀 Features

- Start a Blackjack game for a player
- Automatic player creation on game start
- Draw cards (**HIT**) or finish turn (**STAND**)
- Dealer auto-play logic
- Game resolution (Win / Loss / Push)
- Persistent player ranking
- Reactive API with Spring WebFlux
- Global exception handling
- Swagger UI documentation
- Fully Dockerized environment

---

## 💻 Technologies Used

- Java 17
- Spring Boot 3 (WebFlux, JPA, Reactive MongoDB)
- MySQL 8
- MongoDB 7
- Maven
- Lombok
- Docker & Docker Compose
- Swagger / OpenAPI
- Netty Web Server

---

## 🔗 API Endpoints

The following table summarizes the available REST endpoints exposed by the Blackjack API.
All responses are returned as JSON.

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/game/new` | Create a new Blackjack game (player is created automatically if not exists) | `{ "playerName": "christo" }` |
| GET | `/game/{id}` | Retrieve detailed information about a game | N/A |
| POST | `/game/{id}/play` | Play a move in an existing game (HIT or STAND) | `{ "move": "HIT" }` |
| DELETE | `/game/{id}/delete` | Delete an existing game | N/A |
| GET | `/player/ranking` | Get players ranking based on performance | N/A |
| PUT | `/player/{id}` | Update player name | `{ "name": "christo" }` |

---

## ▶️ Running the Project Locally (Without Docker)

### 1️⃣ Requirements

- JDK 17
- Maven
- Local MySQL running on port 3306
- Local MongoDB running on port 27017

---

### 2️⃣ Clone the repository

```bash
git clone https://github.com/christo256/5.1-Spring-Framework-Blackjack.API.git
```

### 3️⃣ Run the backend
 ```bash
mvn clean install
mvn spring-boot:run
```
Open Swagger UI:
```bash
http://localhost:8080/swagger-ui.html
```

---

## 🐳 Running With Docker (Level 2)

### 1️⃣ Build and start containers
```bash
docker compose up --build
```
This will start:

- blackjack-api

- blackjack-mysql

- blackjack-mongo

Swagger will be available at:
```bash
http://localhost:8080/swagger-ui.html
```

### 2️⃣ Docker Hub Image

Public image available on Docker Hub:

`christo256/blackjack-api:1.0`

Pull the image:

```bash
docker pull christo256/blackjack-api:1.0
```

---

## 🧩 Project Structure

```bash
src/main/java/com/blackjack
├── controller      → REST endpoints
├── service         → Business logic
│   ├── game
│   └── player
├── repository      → Mongo + SQL persistence
├── domain          → Game, Card, Hand, Player...
├── dto             → Request/Response DTOs
├── mapper          → Entity ↔ DTO mapping
└── exception       → GlobalExceptionHandler
```

## 🧠 Architecture Notes

- Reactive flow using `Mono`
- MongoDB manages game state and cards
- MySQL manages ranking and statistics
- Docker Compose orchestrates API and databases
- Healthchecks ensure proper startup order

