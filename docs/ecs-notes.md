## I. Challenges

- [High] Naming convention, best practices and alternative solutions for web services, provisioning and orchestration tools, some of which are complicated and require documentation (e.g., SPA has a lot of common design choices associated with it such as reverse proxy)
- [Low] Reverse proxy, serving static files and handling routing in a SPA (1 domain vs. 2 domains, with 1 domain having the disadvantage of now being compatible with integration with a CDN), and TLS certificates (and, to lesser extent, HTTP versions)
- [High] Transitioning strategy from local Docker Compose deployment to AWS deployment: Research options for ECS deployment:
  - AWS Copilot CLI — _preferred_
    - [Reddit comment](https://old.reddit.com/r/docker/comments/lximf0/best_way_to_deploy_docker_compose_to_aws_ecs/)
    - [AWS Copilot CLI official site](https://aws.github.io/copilot-cli/)
  - Application Load Balancer (ALB) with manual provisioning and orchestration:
    - [Reddit comment](https://old.reddit.com/r/docker/comments/lximf0/best_way_to_deploy_docker_compose_to_aws_ecs/)
  - Docker ECS Integration (Compose CLI with ECS) — _deprecated_
    - ["Docker Compose’s integration with Amazon ECS has been deprecated and is retiring in November 2023"](https://aws.amazon.com/blogs/containers/deploy-applications-on-amazon-ecs-using-docker-compose/)
  - Other useful links:
    - [Reddit Discussion](https://old.reddit.com/r/docker/comments/lximf0/best_way_to_deploy_docker_compose_to_aws_ecs/)
    - [Amazon ECS Workshop](https://ecsworkshop.com/)
- [Moderate] Identity and access management (not the AWS IAM service)
- Numerous configuration options available for ECS task definitions, services, and namespaces (the name of "namespace" is confusing given it's for the cloud resource discovery service), which can be mitigated by the adoption of AWS Copilot CLI, which is opinionated and streamlines the deployment process of containerized app
- [Moderate] Cost considerations (e.g., (1) not choosing EKS, as EKS would likely cost $70+ USD per month, (2) not using NAT Gateway for ECS task defintion sitting in the private subnet to pull image from Docker Hub, (3) RDS vs. VPC endpoints for ECS task definition sitting in the private subnet to pull iamge from ECR, (4) RDS infrastructure costs)
- Networking and load balancing

## II. Namespace and Service Discovery in AWS ECS for Web App Components

Q: I have all task definitions that correspond to different components of a web app, including database component, backend component (an API server) and frontend component (a web server). I used Fargate as my launch type for all task definitions. I'm now transitioning this web app from local deployment to AWS ECS, creating a service for the database component in the cluster called `web-app-cluster`. I've created a new namespace called `electron-eon-namespace` for grouping all services that correspond to my web app's components, and I've chosen "API calls and DNS queries in VPCs" as the instance discovery for the foregoing namespace. Question is, in the "ECS Console > Cluster > Create new service > Service discovery" section, why is it asking me a "Service discovery name", isn't this redundant since the existing namespace I've chosen is for the service discovery.

A [Amazon Q]: The "Service discovery name" you're being asked to provide in the ECS Console is not redundant, even though you've already chosen an existing namespace. Here's why it's necessary and how it fits into your service discovery setup:

1. Namespace vs. Service Name: The namespace you created (`electron-eon-namespace`) is a logical grouping for all your services, while the "Service discovery name" is specific to the individual service you're creating (in this case, your database component).

2. Hierarchical Structure: Think of it as a hierarchical structure:

   - Namespace: `electron-eon-namespace`
     - Service Discovery Name for Database (e.g., "database")
     - Service Discovery Name for Backend API (e.g., "api")
     - Service Discovery Name for Frontend Web Server (e.g., "web")

3. DNS Resolution: When using service discovery, the full DNS name for your service will typically be a combination of the service discovery name and the namespace. For example: `database.electron-eon-namespace`

4. Unique Identification: The service discovery name allows you to uniquely identify and refer to each component of your web application within the chosen namespace.

5. Service-to-Service Communication: Other services in your cluster can use this name to communicate with the specific component. For instance, your backend API service might use `database.electron-eon-namespace` to connect to the database service.

6. Flexibility: This setup provides flexibility to have multiple services of the same type (e.g., multiple database instances) with different service discovery names within the same namespace.

When setting up your database service, choose a service discovery name that clearly identifies its role within your web application ecosystem. This will make it easier for you to manage and for other services to discover and communicate with each component of your application as you transition from local deployment to AWS ECS.

Sources
[1] [Creating an Amazon ECS service that uses Service Discovery - Amazon Elastic Container Service] (https://docs.aws.amazon.com/AmazonECS/latest/developerguide/create-service-discovery.html)
[2] [Service Discovery over regional VPCs with Transit Gateway peering for ECS services using Service Connect? | AWS re:Post] (https://repost.aws/questions/QUWhe7iFg5Q9WAGT5NyQKavg/service-discovery-over-regional-vpcs-with-transit-gateway-peering-for-ecs-services-using-service-connect)
[3] [Creating an Amazon ECS service using the console - Amazon Elastic Container Service] (https://docs.aws.amazon.com/AmazonECS/latest/developerguide/create-service-console-v2.html)

## III. Accessing Amazon EFS from ECS Task Definitions in a VPC

Q: In order for my ECS task definitions sitting in a public subnet of a VPC to read and write an EFS file system that sits in the private subnet of the same VPC, does the said file system need NFS?

A [Amazon Q]: Yes, your ECS task definitions in a public subnet of a VPC can read and write to an EFS file system in the private subnet of the same VPC using NFS (Network File System). Here are some key points to consider:

1. **EFS uses NFS**: Amazon EFS natively uses the NFS protocol (specifically NFSv4) for file system access.

2. **VPC Connectivity**: As long as your ECS tasks and EFS file system are in the same VPC, they can communicate with each other, even if they're in different subnets (public and private).

3. **Security Groups**: Ensure that the security groups associated with your ECS tasks and EFS mount targets allow the necessary NFS traffic (typically on port 2049).

4. **Mount Targets**: You'll need to create EFS mount targets in the private subnet where your EFS file system resides. These act as access points for your ECS tasks.

5. **Task Definition Configuration**: In your ECS task definition, specify the EFS volume and mount point for your containers to access the file system.

6. **Network Routing**: Make sure your VPC's network routing is configured correctly to allow traffic between the public and private subnets.

7. **IAM Permissions**: Ensure that your ECS task execution role has the necessary permissions to access the EFS file system.

By following these considerations, your ECS tasks in the public subnet should be able to read from and write to the EFS file system in the private subnet using the NFS protocol.

Sources
[1] [Controlling network access to Amazon EFS file systems for NFS clients - Amazon Elastic File System](https://docs.aws.amazon.com/efs/latest/ug/NFS-access-control-efs.html)  
[2] [Create an Amazon ECS task definition and mount a file system on EC2 instances using Amazon EFS - AWS Prescriptive Guidance](https://docs.aws.amazon.com/prescriptive-guidance/latest/patterns/create-an-amazon-ecs-task-definition-and-mount-a-file-system-on-ec2-instances-using-amazon-efs.html)  
[3] [Configuring Amazon EFS file systems for Amazon ECS using the console - Amazon Elastic Container Service](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/tutorial-efs-volumes.html)

## IV. ECS Service Error and NAT Gateway as the Solution

An error occurs when trying to create an ECS service based on the `electron-eon-mysql-task-def`: "Task stopped at: 2024-10-05T11:48:25.065Z  
CannotPullContainerError: pull image manifest has been retried 5 time(s): failed to resolve ref docker.io/library/mysql:8.0: failed to do request: Head 'https://registry-1.docker.io/v2/library/mysql/manifests/8.0': dial tcp 34.226.69.105:443: i/o timeout." This error indicates a timeout when attempting to pull the MySQL image from Docker Hub, which is due to the ECS service being assigned to a private subnet without a NAT Gateway, as the VPC provisioning did not enable one. Therefore, even though the problematic ECS service is attached to a security group allowing all egress (and ingress on port 3306 for MySQL), it still cannot access the internet. Private subnets typically require a NAT Gateway or NAT instance to route outbound internet traffic. In short, without a NAT Gateway, the ECS service cannot pull the MySQL image from Docker Hub.

However, the NAT Gateway incurs a ["Price per NAT gateway ($/hour)" rate at $0.045](https://aws.amazon.com/vpc/pricing/), which amounts to around $32.4 USD per month—too expensive. Consequently, using RDS is much cheaper than running a service based on the `electron-eon-mysql-task-def` that requires a NAT Gateway to pull the MySQL image from Docker Hub. It is worth noting that for standard operation and updates, RDS instances in private subnets do not necessarily need a NAT Gateway, as AWS manages the underlying infrastructure. They can function and receive updates from AWS without requiring internet access.

RDS is a fully managed service, and AWS handles many maintenance tasks, including software patching and updates. However, RDS instances may still require outbound internet access in some scenarios, such as:

1. **License Verification**: Some database engines might require internet access for license verification.
2. **Monitoring and Metrics**: Certain monitoring features might send data to external services.

## V. RDS vs. Using VPC Endpoints for ECS Task Definition to Pull Images from ECR

### **1. Context and Key Issues**:

The context here is to optimize an AWS deployment for a multi-container application that includes a MySQL database. A VPC has been provisioned, and there is consideration of the best approach to manage database connectivity and image pulls from ECR. NAT Gateway—which is required for ECS services to pull image from the Docker Hub, however, too expensive. On the other hand, if the image were to be pulled from the Docker Hub to the local machine then re-uploaded/pushed to ECR, for a ECS task definition to pull the database image from ECR (since ECR is a public AWS service), an ECR VPC endpoint would be needed along with necessary security groups. On the other hand, using RDS removes the concern of pulling images from a registry.

Key concerns include ensuring secure access to the database (i.e., placing the database instance or service in a private subnet) while minimizing costs.

### **2. Options and Combination of Solutions Being Weighed**:

**Option 1: RDS (Relational Database Service)**:

- A managed service for hosting the MySQL database.
- Benefits include automated backups, scaling, patch management, and high availability.
- Potentially higher costs due to the managed nature and instance pricing.

**Option 2: ECS with VPC Endpoints**:

- Using ECR with VPC endpoints to securely pull images from ECR.
- This option allows for a more cost-effective setup by avoiding some managed service costs associated with RDS.
- Requires more manual management of the database but offers flexibility and reduced costs.

### **3. Cost Estimation**:

**3.1 Pricing References**:

- https://aws.amazon.com/privatelink/pricing/
- https://aws.amazon.com/rds/mysql/pricing/

**3.2 RDS Cost Estimate**:

1. **Instance Pricing**:

   - **db.t4g.micro**: This is an instance type eligible for the AWS Free Tier, but it may incur costs if the free tier limit is exceeded.
   - Estimated price for **db.t4g.micro** in **us-east-1** is approximately **$0.018 per hour**.

2. **Storage Costs**:

   - **20 GB storage**: Estimated storage cost in RDS is approximately **$0.115 per GB/month**.
   - Monthly cost for storage = **20 GB \* $0.115/GB = $2.30**.

3. **Backup Costs**:
   - RDS offers free backup storage up to the size of the provisioned storage. Additional backup storage would incur costs, but with 20 GB of usage, this is included in the RDS costs.

**Total RDS Cost**:

- **Instance cost**:
  0.018(hourly) × 24(hours) × 30(days) ≈ 12.96 USD/month.
- **Storage cost**: **2.30 USD/month**.
- **Total**:
  ≈ 12.96 + 2.30 = 15.26 USD/month.

**3.3 VPC Endpoint Cost Estimate**:

Assuming using one **VPC interface endpoint** for ECR:

1. **Hourly Cost**:

   - **$0.01/hour** for the endpoint.
   - Monthly cost = 0.01(hourly) × 24(hours) × 30(days) ≈ 7.20 USD/month.

2. **Data Transfer Costs**:
   - Assuming minimal data transfer, estimate pulling **100 GB** of data per month.
   - Data processed cost = 100 GB × 0.01 = 1.00 USD/month.

**Total VPC Endpoint Cost**:

- **Endpoint cost**: **7.20 USD/month**.
- **Data transfer cost**: **1.00 USD/month**.
- **Total**:
  7.20 + 1.00 = 8.20 USD/month

**3.4 Cost Comparison Summary**:

- **RDS Total Estimated Cost**: **~15.26 USD/month**.
- **VPC Endpoint Total Estimated Cost**: **~8.20 USD/month**.

**3.5 Conclusion**:

- The **VPC endpoint** option is **cheaper** than using **RDS** in this scenario.
- For those who value **managed services** and simplicity, **RDS** might be worth the extra cost, but if minimizing expenses and managing the database manually is preferable, **ECS and VPC endpoints** are more economical.

## VI. Location to Run AWS Copilot CLI and Process for AWS Copilot CLI Migration

### Q&A

Q: If a local deployment of a web app using `compose.yaml` consists of a frontend service and a backend service, each in its own directory within a monorepo, how should AWS Copilot CLI be used to convert these services for AWS deployment? Should `copilot init` be run at the monorepo root directory, or separately in each service directory?

A [Amazon Q]: For a scenario with a monorepo containing both frontend and backend services, it is recommended to run `copilot init` **at the monorepo root directory**. Here’s the reasoning and steps to proceed:

1. **Initialize at the root**:  
   Run `copilot init` from the root of the monorepo to create the initial Copilot application structure.

2. **Service setup**:  
   During the initialization process, Copilot will prompt for the first service to create, which can be either the frontend or backend service.

3. **Additional service**:  
   After setting up the first service, the second service can be added using `copilot svc init` from the root directory.

4. **Service configuration**:  
   For each service, Copilot will prompt to specify the service type, Dockerfile location, and other configuration details, allowing for the respective directories to be set for each service.

5. **Environment setup**:  
   Use `copilot env init` to set up the deployment environment(s).

6. **Deploy**:  
   Once both services are configured, they can be deployed using `copilot deploy` from the root directory.

This approach allows managing both services as part of a single Copilot application while maintaining separate directories for each service within the monorepo structure. It also facilitates easier management of shared resources and inter-service communication.

Ensure the Dockerfiles and service configurations are properly adjusted to work correctly in the AWS environment. If more detailed guidance is needed on setting up specific aspects of services with Copilot, further resources can be consulted.

Sources
[1] [Community | Build Efficient CI/CD Pipelines for Connected Microservices in Under an Hour Using AWS Copilot](https://community.aws/tutorials/ci-cd-pipelines-for-two-connected-services-using-aws-copilot)
[2] [Community | Build Efficient CI/CD Pipelines for Connected Microservices in Under an Hour Using AWS Copilot](https://community.aws/content/2djhTaf6woZwSQPAEt94ScYWWDv/ci-cd-pipelines-for-two-connected-services-using-aws-copilot)
[3] [Installing the AWS Copilot CLI - Amazon Elastic Container Service](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/copilot-install.html)

### Addtional Notes

1. After successfully creating a Load Balanced Web Service called `client` (without customizing the relevant `manifest.yml`):

```bash
√ Deployed service client.
Recommended follow-up action:
  - Your service is accessible at http://<elb-url> over the internet.
```

Note that the `<elb-url>` can be found in the output of `copilot svc show -n client`.

2. After successfully creating a Backend Service called `api-server` (without customizing the relevant `manifest.yml`):

```bash
√ Deployed service api-server.
Recommended follow-up action:
  - Your service is accessible at api-server:8081 with Service Connect.
```

## VII. Configuring Service Port for HTTPS in AWS Copilot

TLDR: Port 80

Q: From the prompt generated by AWS Copilot CLI:

```
Workload type: Load Balanced Web Service

What do you want to name this service? [? for help] client
Service name: client Dockerfile: client/Dockerfile

Which port do you want customer traffic sent to? [? for help] (80) ?
The port will be used by the load balancer to route incoming traffic to
this service. You should set this to the port which your Dockerfile
uses to communicate with the internet.

Which port do you want customer traffic sent to? (80)
```

How should I answer this prompt asking me for a port?

A [ChatGPT]: When using an Application Load Balancer (ALB) in ECS, it may be preferable to let the ALB handle SSL termination and forward HTTP traffic to Caddy on port 80. However, if HTTPS is managed entirely at the Caddy layer, port 443 will need to be exposed directly.

Here’s a comparison between **port 80** or **port 443** in an AWS ECS + ALB setup:

### Option 1: **ALB Handles HTTPS Termination (Port 80 for Caddy)**

- **ALB SSL Termination**: The ALB handles HTTPS by attaching an SSL certificate to it. The ALB listens on port 443, terminates SSL, and forwards unencrypted HTTP traffic to the ECS service on port 80 (Caddy).
- **Caddy on Port 80**: In this scenario, Caddy only handles HTTP traffic, simplifying the ECS configuration as Caddy manages the application layer without handling SSL certificates.

This approach is easier since AWS manages SSL certificates and handles HTTPS at the load balancer level.

### Option 2: **Caddy Handles HTTPS (Port 443 for Caddy)**

- **Caddy SSL Termination**: Caddy manages SSL certificates using its automatic HTTPS feature. The ALB forwards **encrypted** traffic to Caddy on **port 443**, and Caddy handles the rest.
- **Caddy on Port 443**: Port 443 must be exposed on Caddy for this setup to work, with Caddy managing SSL certificate termination internally.

This option offers control over SSL at the Caddy layer but adds complexity, particularly around SSL certificate management, as both AWS and Caddy will handle certificates independently.

### Which to choose?

- **For simplicity and AWS-managed SSL**: Use **port 80** in Caddy, with the ALB handling HTTPS.
- **For Caddy-managed SSL**: Use **port 443** in Caddy, allowing Caddy to manage SSL with its auto-HTTPS feature.

The decision depends on whether SSL management is preferred at AWS or Caddy.

## VIII. Consolidate Caddy Data Directory and Configuration Directory into a Single EFS File System for an AWS Copilot Service

### 1. The Overall Solution

Q: Snippet of my `manifest.yml` for storage:

```yaml
storage:
  volumes:
    caddy-data:
      efs: true
      path: /data
      read_only: false
    electron-eon-caddy-config:
      efs: true
      path: /config
      read_only: false
```

When I run `copilot svc deploy -n client -e test`, I received the following message: `X validate manifest against environment "test": validate "storage": cannot specify more than one managed volume per service`. How should I consolidate the `/data` and `/config` into a single EFS file system?

A [Amazon Q]: To consolidate the `/data` and `/config` directories into a single EFS file system for an AWS Copilot service, the `manifest.yml` can be modified as follows:

1. Replace the two separate volume definitions with a single EFS volume:

```yaml
storage:
  volumes:
    caddy-storage:
      efs: true
      path: /mnt/efs
      read_only: false
```

2. Update the application code or configuration to use the new paths:

   - Instead of `/data`, use `/mnt/efs/data`
   - Instead of `/config`, use `/mnt/efs/config`

3. Ensure that the application creates these directories if they don't exist. This can be done in the container's entrypoint script or as part of the application's initialization process:

```bash
mkdir -p /mnt/efs/data /mnt/efs/config
```

4. If a Dockerfile is being used, update any VOLUME instructions to reflect the new mount point.

5. After making these changes, redeploy the service using:

```
copilot svc deploy -n client -e test
```

This approach allows for using a single managed EFS volume while maintaining separate directories for data and configuration files. Ensure all references to these directories in the application code and configuration files are updated accordingly.

For more fine-grained control over the EFS setup, consider setting up the EFS file system manually and referencing it in the Copilot manifest using the `id` parameter instead of `efs: true`.

Sources
[1] [Community | Navigate Your Containerized Apps to Success with AWS Copilot](https://community.aws/content/2bx7S1uWyARNnlfcjlFRGWZSZrf/navigate-your-containerized-apps-to-success-with-aws-copilot)  
[2] [The manifest file - AWS Control Tower](https://docs.aws.amazon.com/controltower/latest/userguide/the-manifest-file.html)  
[3] [Validation rules for manifest files - Rekognition](https://docs.aws.amazon.com/rekognition/latest/customlabels-dg/md-create-manifest-file-validation-rules.html)

### 2. Create a Directory in the Container to Consolidate the Data Directory and the Configuration Directory

A: How do I do this step "Ensure that your application creates these directories if they don't exist. You can do this in your container's entrypoint script or as part of your application's initialization process: `mkdir -p /mnt/efs/data /mnt/efs/config`"? Is it in the `manifest.yml` or in the Dockerfile for this service? Context is AWS Copilot CLI's storage volume (EFS file system).

Q [Amazon Q]: To create the directories `/mnt/efs/data` and `/mnt/efs/config` for an AWS Copilot service using an EFS file system, there are two main options:

1. **Using the Dockerfile**:  
   This method is typically preferred as it ensures the directories are created when the container image is built.

   Add the following lines to the Dockerfile:

   ```dockerfile
   RUN mkdir -p /mnt/efs/data /mnt/efs/config
   ```

   Place this command after any base image specifications and before the application code is copied or run.

2. **Using a container entrypoint script**:  
   If creating the directories when the container starts is preferred, an entrypoint script can be used.

   a. Create a file named `entrypoint.sh` with the following content:

   ```bash
   #!/bin/sh
   mkdir -p /mnt/efs/data /mnt/efs/config
   # Add any other initialization commands here

   # Execute the main container command
   exec "$@"
   ```

   b. In the Dockerfile, copy this script and set it as the entrypoint:

   ```dockerfile
   COPY entrypoint.sh /entrypoint.sh
   RUN chmod +x /entrypoint.sh
   ENTRYPOINT ["/entrypoint.sh"]
   ```

   c. Ensure to set the CMD in the Dockerfile or the command in the Copilot manifest to run the actual application.

After implementing either of these methods, rebuild the Docker image and redeploy the service using the AWS Copilot CLI.

Note that the `manifest.yml` file is used for defining the service configuration, including the EFS volume mount, but it is not typically used for creating directories within the container. This is why either the Dockerfile or an entrypoint script is used for this purpose.

Sources
[1] [Deploying a sample Amazon ECS application using the AWS Copilot CLI - Amazon Elastic Container Service](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/copilot-deploy.html)  
[2] [Community | Navigate Your Containerized Apps to Success with AWS Copilot](https://community.aws/content/2bx7S1uWyARNnlfcjlFRGWZSZrf/navigate-your-containerized-apps-to-success-with-aws-copilot)  
[3] [Volume - AWS Batch](https://docs.aws.amazon.com/batch/latest/APIReference/API_Volume.html)

Q: "Update your application code or configuration to use the new paths: instead of `/data`, use `/mnt/efs/data`; instead of `/config` , use `/mnt/efs/config`." How do I achieve this, since the `/data` and `/config` are [the directories required by official Caddy image by default](https://hub.docker.com/_/caddy).

### 3. Using Environment Variables to Change the File Location for the Data Directory and Configuration Directory

A [Amazon Q]: To update the paths for the official Caddy image while using AWS EFS, follow these steps:

1. **Use [environment variables](https://caddyserver.com/docs/conventions#file-locations)** to override the default Caddy paths:

   - Set `XDG_DATA_HOME` for the data directory.
   - Set `XDG_CONFIG_HOME` for the config directory.

2. **Modify the Dockerfile or task definition** to include these environment variables:

   ```dockerfile
   ENV XDG_DATA_HOME=/mnt/efs/data
   ENV XDG_CONFIG_HOME=/mnt/efs/config
   ```

3. If using a Dockerfile, add these lines after the `FROM` statement.

4. If using an ECS task definition, add these environment variables to the container definition.

5. Ensure that the EFS volume is mounted at `/mnt/efs` in the task definition or Docker Compose file.

6. Update any references to `/data` or `/config` in the Caddy configuration files to use the new paths (`/mnt/efs/data` and `/mnt/efs/config`).

7. If there are any custom scripts or commands that interact with these directories, update them to use the new paths.

By setting these environment variables, Caddy will use the new paths on the EFS volume while maintaining compatibility with the official image. Remember to rebuild the Docker image if changes are made to the Dockerfile, and redeploy the ECS task with the updated configuration.

Sources
[1] [Updating the mount target configuration - Amazon Elastic File System](https://docs.aws.amazon.com/efs/latest/ug/manage-fs-access-update-mount-target-config.html)  
[2] [EFSVolumeConfiguration - Amazon Elastic Container Service](https://docs.aws.amazon.com/AmazonECS/latest/APIReference/API_EFSVolumeConfiguration.html)  
[3] [Configuring replication to new EFS file system - Amazon Elastic File System](https://docs.aws.amazon.com/efs/latest/ug/create-replication.html)
