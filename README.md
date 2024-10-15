# Electron Eon

## 1.0 Project Description

The project created as a part of the personal portfolio to demonstrate the skill of web development and deployment technologies.

This project is created with minimum e-commerce functionalities to demonstrate a full stack web built with PostgreSQL database, Spring Boot API server backend, and React SPA frontend. Deployment technologies demonstrated include Docker, AWS, Terraform, web server configuration and DNS configuration.

## 2.0 Usage

1. Install Docker by following Docker's official instructions.
2. Run the following bash command or equivalent:

   ```bash
   cp .env.example .env
   ```

   Then modify the environment variables to set up the communication between different components of the web app services.

3. Spin up the Docker client (e.g., Docker Desktop on Windows), then run the following command in the project's root directory:

   ```bash
   docker compose up --build
   # or
   docker compose up --build -d
   ```

4. The application will be ready at http://localhost for local deployment. You can access it by entering the link in the browser, or by clicking the '80:80' port mapping next to the 'client' container in Docker Desktop.

## 3.0 Tech Stack

1. PostgreSQL database
2. Spring Boot Java framework, with Spring Data, Spring Security, Spring Web, etc.
3. React Single Page Application (SPA), with Material UI as the component library
4. Caddy as the web server
5. JSON Web Token (JWT) for centralized token management
6. Docker and Docker Compose for local deployment and cloud deployment
7. Amazon Web Services (AWS) for cloud deployment:
   - AWS Copilot CLI to automate the provisioning of ECS task definitions, ECS services, ECS cluster, ECR repos, VPC, EFS, Application Load Balancer, etc.
   - ECS for orchestration
   - RDS as a separately created supporting resource to function as the managed database in the cloud
8. Terraform for provioning the RDS instance
