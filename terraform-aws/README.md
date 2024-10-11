# Terraform AWS Deomonstration

## Usage

1. Make sure [these prerequisites](https://developer.hashicorp.com/terraform/tutorials/aws-get-started/aws-build#prerequisites) are met.

   - Note that you can opt to use [a named profile](https://registry.terraform.io/providers/hashicorp/aws/latest/docs#using-an-external-credentials-process) (the default profile) instead of environment variables to authenticate the Terraform AWS provider.
   - Using a named profile method implies [configuring the AWS CLI with IAM Identity Center (previously known as AWS SSO) authentication](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-sso.html).
   - Using a named profile means you need to configure the `profile` property in the `main.tf` before proceeding with the following steps.

2. Navigate to the files listed below and select the appropriate [module source type](https://developer.hashicorp.com/terraform/language/modules/sources). Two options are already suggested—local paths and the Terraform Registry—with instructions provided in each respective file:

   - `./rds.tf`

3. Run the following bash script (or equivalent): `cp terraform.tfvars.example terraform.tfvars`, then update the following values in `terraform.tfvars`:

   - Change the `vpc_id` value to the ID of the VPC created by AWS Copilot CLI
   - Change the `vpc_private_subnets` value to the list of IDs of private subnets inside the VPC that is created by AWS Copilot CLI
   - Change the `rds_db_password` value to [the desired password for the master DB user](https://github.com/terraform-aws-modules/terraform-aws-rds?tab=readme-ov-file#input_password)

4. Run `terraform init`, `terraform validate`, `terraform plan`, and `terraform apply` in sequence to provision the resources to complement the infrastructure created by AWS Copilot CLI.
