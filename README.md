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

- PostgreSQL database
- Spring Boot Java framework, with Spring Data, Spring Security, Spring Web, etc.
- React Single Page Application (SPA), with Material UI as the component library
- Caddy as the web server
- JSON Web Token (JWT) for centralized token management
- Docker and Docker Compose for local deployment and cloud deployment
- Amazon Web Services (AWS) for cloud deployment:
  - AWS Copilot CLI to automate the provisioning of ECS task definitions, ECS services, ECS cluster, ECR repos, VPC, EFS, Application Load Balancer, etc.
  - ECS for orchestration
  - RDS as a separately created supporting resource to function as the managed database in the cloud
- Terraform for provioning the RDS instance

## 4.0 Copilot Deployment Steps

If the app and/or environment were not set up before:

- Make sure that AWS Copilot CLI is installed
- Log in the AWS with `aws sso login` or alternatives
- Run `copilot init` at the root directory of this monorepo
  - Give it an app name, e.g., `electroneon`
  - (Optional) define an environment, and if it is a new environment, make sure to configure `http.public.certificates` section
  - Choose any one of the services defined in the `./copilot` directory: (1) `api-server` (after entering `api-server`, Copilot will automatically figure out that there is a service already defined in the `./copilot` directory) as "Backend Service" and (2) `client` as "Load Balanced Web Services"
  - If there is any error related to ECR (Elastic Container Registry), use `aws ecr get-login-password --region <regions_name> | docker login --username AWS --password-stdin <aws_account_id>.dkr.ecr.<regions_name>.amazonaws.com` to authenticate with ECR. Note that this step also requires the installation of AWS CLI and Docker. For more detailed instructions, see [Getting Started with Amazon ECR](https://docs.aws.amazon.com/AmazonECR/latest/userguide/getting-started-cli.html).
- Provision the RDS instance, with proper configuration (e.g., username is `postgres`)

If the app was already set up, but the services were deleted to save costs:

- Run `copilot svc deploy -n client`
- Run `copilot svc deploy -n api-server`
- Provision the RDS instance, with proper configuration (e.g., username is `postgres`)

To delete the services in order to save costs:

- Run `copilot svc delete -n api-server -e prod`
- Run `copilot svc delete -n client -e prod`
- Delete the RDS instance, with proper backup of data or configuration

## 5.0 Copilot Deploy Process

## 5.0 Roadmap

- To be added
