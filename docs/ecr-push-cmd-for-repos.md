## Push Commands for ECR Repositories:

```bash
aws sso login
aws ecr get-login-password --region <regions_name> | docker login --username AWS --password-stdin <aws_account_id>.dkr.ecr.<regions_name>.amazonaws.com
# or
aws sso login --profile <profile-name>
aws ecr get-login-password --region <regions_name> --profile <profile-name> | docker login --username AWS --password-stdin <aws_account_id>.dkr.ecr.<regions_name>.amazonaws.com
```

```bash
docker tag electron-eon-client:latest <aws_account_id>.dkr.ecr.<regions_name>.amazonaws.com/<repo-name-1>:latest
docker tag electron-eon-api-server:latest <aws_account_id>.dkr.ecr.<regions_name>.amazonaws.com/<repo-name-2>:latest
```

```bash
docker push <aws_account_id>.dkr.ecr.<regions_name>.amazonaws.com/<repo-name-1>:latest
docker push <aws_account_id>.dkr.ecr.<regions_name>.amazonaws.com/<repo-name-2>:latest
```

For more detailed instructions, see [Getting Started with Amazon ECR](https://docs.aws.amazon.com/AmazonECR/latest/userguide/getting-started-cli.html).
For authentication with a profile name, see [this SO answer](https://stackoverflow.com/questions/54232443/problem-in-getting-result-from-aws-ecr-get-login/54234613#54234613).
