# Global Stock Tracker API

Backend Spring Boot API for user authentication, stock chart lookup, and watchlist management.

## Run locally

Required environment variables:

```properties
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/global_stock_tracker
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SIGNERKEY=0123456789abcdef0123456789abcdef
OAUTH_CLIENTID=your_google_client_id
OAUTH_SECRET=your_google_client_secret
ALPHAVANTAGE_APIKEY=your_alpha_vantage_key
APP_CORS_ALLOWED_ORIGINS=http://localhost:5173
APP_OAUTH2_SUCCESS_REDIRECT_URL=http://localhost:5173/oauth2/success
```

Start:

```bash
./mvnw spring-boot:run
```

Test:

```bash
./mvnw test
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Response Format

Most successful responses use:

```json
{
  "status": 200,
  "message": "Message",
  "data": {}
}
```

Validation errors:

```json
{
  "status": 400,
  "message": "Validation failed",
  "data": {
    "fieldName": "Error message"
  }
}
```

Authenticated endpoints require:

```http
Authorization: Bearer <accessToken>
```

## Auth

### Register

`POST /users/register`

```json
{
  "email": "user@example.com",
  "username": "quynh",
  "password": "123456"
}
```

### Login

`POST /auth/login`

```json
{
  "username": "quynh",
  "password": "123456"
}
```

Response data:

```json
{
  "token": "access-token",
  "refreshToken": "refresh-token",
  "authenticated": true
}
```

### Refresh Token

`POST /auth/refresh-token`

```json
{
  "token": "refresh-token"
}
```

### Logout

`POST /auth/logout`

```json
{
  "token": "access-token"
}
```

## Stocks

Public:

`GET /stocks/chart?symbol=AAPL`

`GET /stocks/info?symbol=AAPL`

Authenticated:

`GET /stocks/watchlist/{watchlistId}`

`POST /stocks/{watchlistId}/stocks?symbol=AAPL`

## Watchlists

Authenticated:

`GET /watch-list/`

`GET /watch-list/{id}`

`GET /watch-list/user/{userId}`

`POST /watch-list/`

```json
{
  "name": "Tech Stocks",
  "userId": 1
}
```

`PUT /watch-list/{id}`

```json
{
  "name": "Long Term",
  "userId": 1
}
```

`DELETE /watch-list/{id}`

## Users

Authenticated:

`GET /users/`

`PUT /users/{id}`

```json
{
  "email": "new@example.com",
  "username": "newname",
  "password": "newpassword"
}
```

`DELETE /users/{id}`
