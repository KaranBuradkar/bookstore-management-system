# 📘 Bookstore Backend (Spring Boot)

A scalable, modular, and secure backend for bookstore management, built with Spring Boot 3. The system supports user authentication, JWT-based authorization, Google OAuth2 login, product management, orders, payments, and robust exception handling.
The architecture follows a clean layered approach: Controller → Service → Repository → Entity.

---

## 🚀 Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security (JWT Authentication)**
- **Spring Data JPA / Hibernate**
- **MapStruct Mapper (DTO ↔ Entity mapping)**
- **Maven**
- **MySQL**
- **OAuth2 Login (Google)**

---

## 📦 Project Setup

### Folder Structure

```bash
src/main/java/com/bookstore
│
├── config            # Security, JWT, CORS, ModelMapper configs
├── controller        # REST controllers
├── service           # Business logic
├── repository        # JPA Repositories
├── entity            # JPA entities
├── dto               # Request/response DTOs
├── exception         # custom exceptions
└── security          # JWT filter, auth provider, user details
```

---

## 🔐 API Overview

### **Authentication**

- `POST /auth/register` — Register new user
- `POST /auth/login` — Username/password login (returns JWT token)
- `GET /auth/refresh_token` — Refresh access token

### **Users**

- `GET /v1/users` — Fetch list of users
- `GET /v1/users/{id}` — Fetch single of user
- `DELETE /v1/users/{id}` — delete user

### **Address**

- `GET /v1/address` — List all addresses
- `GET /v1/address/{id}` — Get single address
- `POST /v1/address` — Add a new address
- `PUT /v1/address/{id}` — Update address
- `DELETE /v1/address/{id}` — Delete address

### **Address**

- `GET /v1/authors` — List all authors
- `GET /v1/authors/{id}` — Get single author
- `POST /v1/authors` — Add a new author
- `PUT /v1/authors/{id}` — Update author
- `DELETE /v1/authors/{id}` — Delete author

### Books

- `GET /v1/books` — Get paginated list of books (detailed response)
- `GET /v1/books/short` — Get paginated list of books (short response)
- `GET /v1/books/{id}` — Fetch book details by ID
- `POST /v1/books` — Create a new book
- `PUT /v1/books/{id}` — Update complete book details
- `PATCH /v1/books/{id}` — Partially update book fields
- `DELETE /v1/books/{id}` — Delete a book by ID

### Book–Author Relations

- `PUT /v1/books/{bookId}/authors/{authorId}` — Add an author to a book
- `PUT /v1/books/{bookId}/authors/{authorId}/replace-by/{newAuthorId}` — Replace an author in the book
- `DELETE /v1/books/{bookId}/authors/{authorId}` — Remove an author from the book

### Customers

- `GET /v1/customers` — Get paginated list of customers
- `GET /v1/customers/{id}` — Fetch customer details by ID
- `PUT /v1/customers/{id}` — Update full customer details
- `PATCH /v1/customers/{id}` — Partially update customer fields
- `DELETE /v1/customers/{id}` — Delete a customer by ID

### Orders

- `GET /v1/orders` — List all orders (supports pagination, sorting, filtering by customerId and status)
- `GET /v1/orders/{id}` — Get order details by ID
- `POST /v1/orders` — Create a new order
- `PUT /v1/orders/{id}` — Update an existing order
- `PATCH /v1/orders/{id}` — Partially update order fields
- `DELETE /v1/orders/{id}` — Delete an order by ID

### Payments
- `GET /v1/payments` — List all payments (supports pagination, sorting, optional filtering by customerId `cId`)
- `GET /v1/payments/{id}` — Get payment details by ID
- `POST /v1/payments` — Create a new payment
- `PUT /v1/payments/{id}` — Update an existing payment
- `PATCH /v1/payments/{id}` — Partially update payment fields
- `DELETE /v1/payments/{id}` — Delete a payment by ID

### Payment Methods
- `GET /v1/payment-methods` — List all payment methods (supports pagination and sorting)
- `GET /v1/payment-methods/{id}` — Get payment method details by ID
- `POST /v1/payment-methods` — Create a new payment method
- `PUT /v1/payment-methods/{id}` — Update an existing payment method
- `DELETE /v1/payment-methods/{id}` — Delete a payment method by ID

### Reviews
- `GET /v1/reviews` — List all reviews (supports pagination, sorting, optional filtering by bookId)
- `GET /v1/reviews/{id}` — Get review details by ID
- `POST /v1/reviews` — Create a new review
- `PUT /v1/reviews/{id}` — Update an existing review
- `PATCH /v1/reviews/{id}` — Partially update review fields
- `DELETE /v1/reviews/{id}` — Delete a review by ID


> _All secured endpoints require passing JWT in header:
> Authorization: Bearer {jwt-token}

### Example Request/Response
1. Login Request:
    ```json lines
   {
      "email": "user@example.com",
      "password": "password123"
    }
   ```
2. Login Response
    ```json lines
    {
      "user_id": 1,
      "role_type": ["CUSTOMER"],
      "roles_ids": [1],
      "access_token": "eyJhbGciOiJIUz...",
      "refresh_token": "eyJhbGciOiJI..."
    }
    ```
---

## 🖥️ How to Run Locally

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/Bookstore-Management-System.git
    cd Bookstore-Management-System
   ```

2. Create `application.properties` in `src/main/resources/`:
   ```properties
    # MySql
    spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
    spring.datasource.username=root
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=none

    # JWT
    application.security.jwt.secret-key=${JWT_SECRET}
    application.security.jwt.access-token-expiration=86400000
    application.security.jwt.refresh-token-expiration=604800000
    
    # OAuth2
    spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
    spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
    spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google
    spring.security.oauth2.client.registration.google.scope=openid,profile,email
    ```

3. Start the Application:
    ```bash
    mvn spring-boot:run
   ```

## 👨‍💻 Author

- **Karan Buradkar**  
  [LinkedIn](https://linkedin.com/in/karan-buradkar-70361a258) | [GitHub](https://github.com/KaranBuradkar)


