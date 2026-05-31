# Fashion Backend

Backend REST API cho hệ thống Fashion, xây dựng bằng Spring Boot. Project xử lý xác thực người dùng, quản lý sản phẩm, bộ sưu tập, dịch vụ, đơn hàng, lịch hẹn, wishlist, email, thanh toán SePay, upload ảnh Cloudinary, Firebase và AI chat qua Groq.

## Công nghệ sử dụng

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

## Yêu cầu môi trường

- JDK 21
- MySQL 8 hoặc tương thích
- Maven không bắt buộc vì repo đã có Maven Wrapper
- Tài khoản hoặc credential cho các dịch vụ ngoài nếu dùng đầy đủ tính năng: Cloudinary, Gmail App Password, Firebase, SePay, Groq

## Cấu hình database

Tạo database MySQL:

```sql
CREATE DATABASE fashion_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Ứng dụng mặc định chạy với:

```properties
server.port=8080
server.servlet.context-path=/fashion
spring.datasource.url=jdbc:mysql://localhost:3306/fashion_db
spring.jpa.hibernate.ddl-auto=update
```

Base URL local:

```text
http://localhost:8080/fashion
```

## Cấu hình biến môi trường

Các cấu hình đang nằm trong `src/main/resources/application.properties`. Khi chạy local hoặc deploy, nên override bằng biến môi trường thay vì commit secret thật vào source code.

| Chức năng | Property | Gợi ý env |
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

JWT signer key cần đủ dài cho thuật toán HS512, tối thiểu 64 ký tự.

## Chạy project local

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Build file JAR:

```powershell
.\mvnw.cmd clean package
```

Chạy JAR sau khi build:

```powershell
java -jar target\fashion_db-0.0.1-SNAPSHOT.jar
```

## Test

```powershell
.\mvnw.cmd test
```

Hiện project có test khởi động context Spring Boot tại `src/test/java/com/example/fashion_db/FashionDbApplicationTests.java`.

## Docker

Build image:

```powershell
docker build -t fashion-be .
```

Run container:

```powershell
docker run --name fashion-be -p 8080:8080 fashion-be
```

Lưu ý: `pom.xml` đang dùng Java 21. Nếu build Docker gặp lỗi `invalid target release: 21`, hãy đổi image trong `Dockerfile` sang bản JDK 21.

## Cấu trúc thư mục chính

```text
src/main/java/com/example/fashion_db
├── configuration    # Security, JWT, CORS, Cloudinary, Firebase
├── controller       # REST controllers
├── dto              # Request/response models
├── entity           # JPA entities
├── enums            # Enum domain
├── exception        # Global exception handling
├── job              # Quartz jobs
├── mail             # Email services
├── mapper           # MapStruct mappers
├── repository       # Spring Data repositories
├── service          # Business logic
├── specification    # Dynamic query filters
└── utils            # Utility helpers
```

## Xác thực và phân quyền

API dùng JWT Bearer Token qua Spring Security OAuth2 Resource Server.

Header cho các API cần đăng nhập:

```http
Authorization: Bearer <access_token>
```

Các API public chính:

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

Các API còn lại mặc định cần token hợp lệ.

## Nhóm API chính

Base path: `/fashion`

| Nhóm | Path |
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

## Ví dụ request

Đăng nhập:

```http
POST /fashion/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password"
}
```

Lấy danh sách sản phẩm:

```http
GET /fashion/product?page=1&size=10
```

Gọi API cần đăng nhập:

```http
GET /fashion/users/myInfo
Authorization: Bearer <access_token>
```

## Lưu ý bảo mật

- Không commit credential thật của database, Cloudinary, Gmail, Firebase, SePay hoặc Groq.
- Nên dùng biến môi trường hoặc profile riêng cho local, staging và production.
- File Firebase service account nên đặt ngoài source code khi deploy, ví dụ `/root/firebase-service-account.json`.
- Nếu repository đã từng public secret, cần rotate lại các key tương ứng.
