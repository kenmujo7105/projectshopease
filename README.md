# 🛒 Ecommerce Shop - Spring Boot Project

Dự án web thương mại điện tử đơn giản sử dụng Spring Boot, Maven và MySQL.

---

## 🚀 Build & Run

### ✅ Yêu cầu

- Java 17+
- Maven 3.8+
- MySQL
---

## ⚙️ Công nghệ sử dụng

- 💻 Spring Boot 3+
- 🔐 Spring Security + JWT
- 🛢️ Hibernate / JPA
- 🐬 MySQL 
- 🛠️ MapStruct
- 🌐 Swagger 3 (OpenAPI)
- 🐳 Docker (optional)

---
### 🔧 Build Dự Án
Tạo file .env theo chuẩn sau:
```declarative
MYSQL_PASSWORD=123456
MYSQL_USERNAME=root
MYSQL_DATABASE=shop-db
MYSQL_PORT=3306
MYSQL_HOST=localhost

REDIS_HOST=localhost
REDIS_PORT=6379
#
JWT_ACCESS_KEY="1TjXchw5FloESb63Kc+DFhTARvpWL4jUGCwfGWxuG5SIf/1y/LgJxHnMqaF6A/ij"
JWT_REFRESH_KEY="8u)=}s@pq9_o4&mv]1ezze&c}{!?]$hjt@xz&*((bc7{34&]n8@v?cunwetpwm?["

#
GHN_TOKEN="<ghn-token>"
GHN_SHOP_ID="<shop-id>"
GHN_BASE_URL="https://online-gateway.ghn.vn/shiip/public-api"

#
CLOUDINARY_LINK="cloudinary://<your-privatekey>"

#
//ADMIN_EMAIL="admin@gmail.com"
//ADMIN_PASSWORD="admin"

#
EMAIL_USERNAME="<your-email>"
EMAIL_PASSWORD="<your-app-password> not your email password, to find it please enable 2-step verification and [→] https://myaccount.google.com/apppasswords"
```
```bash
mvn clean install

mvn spring-boot:run
```
## 📁 Cấu trúc thư mục chính
```
src/
└── main/
    ├── java/
    │   └── com/
    │       └── EcommerceShop/
    │           └── Shop/
    │               ├── config/
    │               ├── controller/    
    │               ├── dto/
    │               │   └── request/
    │               │   └── response/
    │               ├── entity
    │               ├── enums
    │               ├── exception/
    │               ├── mapper/
    │               ├── repository/
    │               ├── service/
    │               │   └── serviceImpl
    │               │   └── v3Service
    │               └── ShopApplication.java
    └── resources/
        ├── application.yml
└── test/
.env

```
