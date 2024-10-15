# Electron Eon

## 1.0 Project Description

The project created as a part of the personal portfolio to demonstrate the skill of web development and deployment technologies.

This project is created with minimum e-commerce functionalities to demonstrate a full stack web built with PostgreSQL database, Spring Boot API server backend, and React SPA frontend. Deployment technologies demonstrated include Docker, AWS, Terraform, web server configuration and DNS configuration.

## 2.0 Usage

Note that the instructions below would require more manual work and are more prone to error than using Docker Compose, as outlined in the `README.md` in the project root directory, to run this full stack web app.

### 2.1 Prerequisite

1. PostgreSQL client installed (on Windows, you may also need to [set up several environment variables](https://www.youtube.com/watch?v=0EBkVzIBnoc))
2. Java Development Kit 21+ such as [Eclipse Temurin™ 21.0.4+7-LTS](https://adoptium.net/temurin/releases/?package=jdk&os=any)
3. IDE for Java such as IntelliJ Community Edition
4. IDE for JavaScript such as VS Code

### 2.2 Steps without Docker Compose

1. Clone the repo and resolve Maven dependencies as needed:

   ```bash
   git clone https://github.com/mg86837242/electron-eon.git
   ```

2. Navigate to the `api-server` directory, then create `application.properties` based on `application.properties.example:

   ```bash
   cd api-server
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

   Modify the following properties to match your [PostgreSQL credentials to connect to you local database](https://www.postgresql.org/docs/current/app-psql.html):

   ```
   # application.properties
   spring.datasource.username=********
   spring.datasource.password=********
   ```

3. Follow the instructions of `src/main/resources/certs/README.md` to create and set up keys for JWT authentication.

4. Start the Spring Boot application `src/main/java/dev/sy/ElectronEonApplication.java` by using an IDE (e.g., IntelliJ Community Edition).

5. Navigate to the `client` directory, open up another terminal or another IDE (e.g., VS Code), then install dependencies:

   ```bash
   cd client
   npm install
   ```

6. Start the front-end server and follow the Vite prompts in the terminal to view the app in the browser

   ```bash
   npm run dev
   ```

## 3.0 Overview of Technology Stack

- Database Management System: PostgreSQL
  - Reason: PostgreSQL is both free and easy to set up (especially in Linux
    system). In addition, the developer is familiar with this database
    management system. Due to the project's tight deadline, both ease of use and
    familiarity hold significant value.
- Server: Spring Framework including Spring Data, Spring Web, Spring Security
  with JWT, etc.
  - Programming language: Java
  - Reason: Using Java makes it easy to adhere to SOLID and OO design
    principles, which are among the technical requirements of this project.
    Additionally, the developer is familiar with Java for backend
    development.
- Client: SPA by using React and its sub-libraries, Vite for front end
  development server and bundler, MUI for component library, etc.
  - Programming language: JavaScript, JSX
  - Reason: Setting up an SPA is easy. Furthermore, the developer is familiar
    with this type of web architecture. Given the tight deadline for this
    project, both ease of use and familiarity are highly valued.

## 4.0 Functional Requirements

- User Authentication and Authorization:
  - Users should be able to create an account, log in and log out.
  - Users should have different roles (e.g., regular user, admin) with
    corresponding permissions.
- Product Browsing and Searching:
  - Users should be able to view products by category.
  - Users should be able to search for products by keyword.
- Product Management:
  - Admins should be able to view, add, update and delete products.
  - Products should have attributes such as name, description, price, and
    category.
- Shopping Cart:
  - Users should be able to view, add, update and delete the contents of their
    shopping cart.
  - Users should be able to proceed to checkout from the shopping cart.
- Checkout Process:
  - Users should be able to use a form to view and add information (such as
    address information) to the order throughout the checkout process.
  - Users should be able to view confirmation of their order after completing
    checkout.t
- Order Management:
  - Admins should be able to view, add, update and delete orders.
  - Orders should possess attributes like product, quantity, address, and
    creation date. Meanwhile, price information can be derived from the
    products table.
- Responsive Design:
  - The web app should be responsive and accessible across different devices
    and screen sizes.
- **Scope Exclusion**:
  - Payment integration:
    - Users should be able to pay for orders using various payment methods (
      e.g., credit card, PayPal).
    - Payment processing should be secure and compliant with relevant
      regulations.
  - Security and Privacy:
    - User data should be securely stored and protected.
    - The web app should comply with applicable privacy laws and
      regulations (e.g., PDPA, GDPR).
  - User Account Management:
    - Users should be able to view and update their account information.
    - Users should be able to update their password.
    - Admins should be able to view, add, update and delete users.

## 5.0 Technical Requirements

- Design and analysis:
  - Presentation slides
  - Use Case Diagram
  - Class Diagram
  - ERD
  - Design patterns, with currently identified patterns include but not
    limited to:
    - Design Patterns: Elements of Reusable Object-Oriented Software
      - Factory Method to create EntityManager, which is provided by the
        Hibernate
      - Object Pool incorporated into HikariCP, which the default
        option adopted by Spring Boot
      - Builder for Entity classes and DTOs (Data Transfer Objects)
      - Decorator for implementing UserDetails interface
      - Composite design pattern integrated into React
      - Observer design pattern incorporated into React, e.g., passing
        props and useEffect hook
      - State design pattern adopted and heavily utilized by React
    - Other design patterns
      - MVC for the designing the web application
      - DAO design pattern in Java
  - Database normalization process
  - Illustration of MVC
  - Overview of technology stack
- Code implementation:
  - Demonstration of functionalities mentioned in Functional Requirements
  - Logging framework - slf4j
  - Source control - Git
  - Validation and authentication
  - TDD and appropriate coverage - JUnit5 and Mockito
  - Data access framework - Spring Data, which includes JDBC, JPA, etc.
- Project management:
  - Retrospection - Sprint Retrospective

## 6.0 Sprint Retrospectives

- 29 Apr - 30 Apr
  - What went well?: project management documentation, project bootstrapping,
    and database design
  - What went wrong?: time-consuming process of configuring and debugging
    JPA, confusion between model and ORM layers, and unclear file structure
    for model and ORM layers
    - What changes can I make?: looking for examples
- 2 May - 3 May
  - What went well?: coding models and DAOs, and testing by using sample
    data and scripts
  - What went wrong?: time-consuming process of writing Hibernate entity
    operations without the help of Spring Data, and laborious unit testing
    procedure because the EntityManager is application-managed rather than
    container-managed
  - What changes can I make?: relying on Dependency Injection for ease of
    testing
- 6 May - 7 May
  - What went well?: bootstrapping Spring Boot project, and porting the
    code from the old project to the new one, which is facilitated by
    utilizing the DAO to manage different implementations of data access
  - What went wrong?: configuring Spring Data
  - What changes can I make? not wasting time on the Hibernate entity manager approach and DAO, since JpaRepository can replace the functionalities of DAO
- 8 May - 9 May
  - What went well?: coding stubs and error handling
  - What went wrong?: authorization
  - What changes can I make? nothing
- 10 May - 13 May
  - What went well?: authorization by utilizing token mechanics
  - What went wrong?: modeling response
  - What changes can I make? using JPA projections
- 14 May - 15 May
  - What went well?: state management
  - What went wrong?: project configuration, authentication and authorization
  - What changes can I make? focusing on the core business logics
- 16 May - 17 May
  - What went well?: styling
  - What went wrong?: protected routes
  - What changes can I make? leveraging server-side state management tool to code the authentication, so boilerplate code can be reduced, (this still needs
    further discussions)
- 20 May - 21 May
  - What went well?: styling, cart functionalities
  - What went wrong?: multi-step forms can't be handled by form library
    and its associated libraries due to multi-stage submission
  - What changes can I make? handling multi-step form submission in the backend (but it takes a lot of time to design the backend logics); focusing on the core
    business logics rather than handling forms
