# Desafio: Criar ma API RESTful para encurtar URLs, redirecionar links curtos para o original e obter estatísticas de uso.

## Tecnologias

- Java 17
- Spring Boot 3.5.4
- MongoDB
- Swagger OpenAPI 3 (springdoc-openapi 2.8.9)
- Docker + Docker Compose

---

## Endpoints da API

### Encurtar uma URL
`POST /api/v1/urls`

```json
{
  "originalUrl": "https://example.com"
}
```

Retorno:
```json
{
  "shortUrl": "http://localhost:8080/AbC123",
  "originalUrl": "https://example.com",
  "createdAt": "2025-08-04T03:52:16.544Z"
}
```

---

### Redirecionar para a URL original
`GET /api/v1/urls/{shortCode}`

Exemplo: `GET /api/v1/urls/AbC123`  
Redireciona com `302 FOUND` para a URL original.

---

### Estatísticas da URL encurtada
`GET /api/v1/urls/{shortCode}/stats`

Retorno:
```json
{
  "shortUrl": "http://localhost:8080/AbC123",
  "originalUrl": "https://example.com",
  "totalAccesses": 5,
  "averageAccessesPerDay": 2.5
}
```

---

## Como rodar localmente

### Pré-requisitos
- Java 17
- Docker e Docker Compose
- Maven 3+

### 0. Rodar o Clean Package
```bash
./mvnw clean package
```

### 1. Subir a aplicação com Docker
```bash
docker-compose up --build
```

Acesse a API: http://localhost:8080  
Swagger UI: http://localhost:8080/swagger-ui.html

### 2. Rodar localmente com Maven
```bash
./mvnw spring-boot:run
```

MongoDB precisa estar rodando localmente ou via Docker:
```bash
docker run -d -p 27017:27017 --name mongo mongo
```

---

## ✅ Testes

```bash
./mvnw test
```

Inclui testes com Testcontainers + JUnit para operações com MongoDB.

---