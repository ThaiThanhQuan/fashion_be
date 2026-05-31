# Fashion Backend

REST API backend for the Fashion system, built with Spring Boot. The project handles user authentication, product management, collections, services, orders, appointments, wishlists, email notifications, SePay payments, Cloudinary image uploads, Firebase integration, and AI chat through Groq.

## Tech Stack

- Java 21
- Spring Boot 4.0.5
- Spring Web, Spring Security, OAuth2 Resource Server
- Spring Data JPA, MySQL
- Maven Wrapper
- Lombok, MapStruct
- Cloudinary SDK
- Java Mail Sender
- Firebase Admin SDK
- Quartz Scheduler
- Docker

## Requirements

- JDK 21
- MySQL 8 or compatible
- Maven is optional because the repository includes Maven Wrapper
- External service credentials if you want to use all integrations: Cloudinary, Gmail App Password, Firebase, SePay, Groq

## Database Setup

Create the MySQL database:

```sql
CREATE DATABASE fashion_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Default application settings:

```properties
server.port=8080
server.servlet.context-path=/fashion
spring.datasource.url=jdbc:mysql://localhost:3306/fashion_db
spring.jpa.hibernate.ddl-auto=update
```

Local base URL:

```text
http://localhost:8080/fashion
```

## Environment Configuration

Runtime configuration is currently defined in `src/main/resources/application.properties`. For local development and deployment, override sensitive values through environment variables instead of committing real secrets to source control.

| Feature | Property | Suggested env |
| --- | --- | --- |
| MySQL URL | `spring.datasource.url` | `SPRING_DATASOURCE_URL` |
| MySQL username | `spring.datasource.username` | `SPRING_DATASOURCE_USERNAME` |
| MySQL password | `spring.datasource.password` | `SPRING_DATASOURCE_PASSWORD` |
| JWT signing key | `jwt.signerKey` | `JWT_SIGNERKEY` |
| Cloudinary cloud name | `cloudinary.cloud-name` | `CLOUDINARY_CLOUD_NAME` |
| Cloudinary API key | `cloudinary.api-key` | `CLOUDINARY_API_KEY` |
| Cloudinary API secret | `cloudinary.api-secret` | `CLOUDINARY_API_SECRET` |
| Mail username | `spring.mail.username` | `SPRING_MAIL_USERNAME` |
| Mail password | `spring.mail.password` | `SPRING_MAIL_PASSWORD` |
| Firebase service account | `firebase.service-account-path` | `FIREBASE_SERVICE_ACCOUNT_PATH` |
| Frontend URL | `app.frontend-url` | `APP_FRONTEND_URL` |
| SePay API key | `sepay.api-key` | `SEPAY_API_KEY` |
| SePay webhook secret | `sepay.webhook-secret` | `SEPAY_WEBHOOK_SECRET` |
| Groq API key | `groq.api-key` | `GROQ_API_KEY` |

The JWT signing key must be long enough for HS512. Use at least 64 characters.

## Run Locally

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Build the JAR:

```powershell
.\mvnw.cmd clean package
```

Run the built JAR:

```powershell
java -jar target\fashion_db-0.0.1-SNAPSHOT.jar
```

## Tests

```powershell
.\mvnw.cmd test
```

The project currently includes a Spring Boot context startup test at `src/test/java/com/example/fashion_db/FashionDbApplicationTests.java`.

## Docker

Build the image:

```powershell
docker build -t fashion-be .
```

Run the container:

```powershell
docker run --name fashion-be -p 8080:8080 fashion-be
```

Note: `pom.xml` uses Java 21. If Docker build fails with `invalid target release: 21`, update the images in `Dockerfile` to JDK 21 variants.

## Main Project Structure

```text
src/main/java/com/example/fashion_db
├── configuration    # Security, JWT, CORS, Cloudinary, Firebase
├── controller       # REST controllers
├── dto              # Request/response models
├── entity           # JPA entities
├── enums            # Domain enums
├── exception        # Global exception handling
├── job              # Quartz jobs
├── mail             # Email services
├── mapper           # MapStruct mappers
├── repository       # Spring Data repositories
├── service          # Business logic
├── specification    # Dynamic query filters
└── utils            # Utility helpers
```

## Authentication and Authorization

The API uses JWT Bearer Tokens through Spring Security OAuth2 Resource Server.

Header for protected APIs:

```http
Authorization: Bearer <access_token>
```

Main public APIs:

- `POST /fashion/auth/register`
- `POST /fashion/auth/login`
- `POST /fashion/auth/google`
- `POST /fashion/auth/refresh`
- `POST /fashion/auth/introspec`
- `POST /fashion/auth/logout`
- `POST /fashion/auth/forgot-password`
- `POST /fashion/auth/reset-password`
- `GET /fashion/product/**`
- `GET /fashion/collections/**`
- `GET /fashion/services/**`
- `GET /fashion/artists/**`
- `GET /fashion/search`
- `POST /fashion/payment/sepay-webhook`
- `POST /fashion/ai/chat`

All other APIs require a valid access token by default.

## Main API Groups

Base path: `/fashion`

| Group | Path |
| --- | --- |
| Auth | `/auth` |
| Users | `/users` |
| Roles | `/roles` |
| Products | `/product` |
| Product images | `/product_images` |
| Product variants | `/product_variants` |
| Product categories | `/category_product` |
| Collections | `/collections` |
| Collection categories | `/category-collections` |
| Seasons | `/seasons` |
| Artists | `/artists` |
| Services | `/services` |
| Pricing | `/pricing` |
| Highlights | `/highlights` |
| Timelines | `/timelines` |
| Workflows | `/workflows` |
| Orders | `/orders` |
| Addresses | `/address` |
| Wishlist | `/wishlists` |
| Subscribers | `/subscribers` |
| Appointments | `/appointments` |
| Search | `/search` |
| Payment webhook | `/payment/sepay-webhook` |
| AI chat | `/ai/chat` |

## Request Examples

Login:

```http
POST /fashion/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password"
}
```

Get products:

```http
GET /fashion/product?page=1&size=10
```

Call a protected API:

```http
GET /fashion/users/myInfo
Authorization: Bearer <access_token>
```

## Security Notes

- Do not commit real credentials for the database, Cloudinary, Gmail, Firebase, SePay, or Groq.
- Use environment variables or separate profiles for local, staging, and production.
- Keep the Firebase service account file outside the source tree in deployment, for example `/root/firebase-service-account.json`.
- If this repository has ever exposed real secrets, rotate the affected keys.
