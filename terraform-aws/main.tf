########################################################################
# Main Module
# Referenced example: https://github.com/terraform-aws-modules/terraform-aws-vpc/blob/v5.13.0/examples/complete/main.tf
########################################################################

provider "aws" {
  region = local.region
  # Change the profile name in the `variable.tf`, or uncomment this
  # argument if environment variables were to be used to authenticate
  # the Terraform AWS provider
  # @see: https://registry.terraform.io/providers/hashicorp/aws/latest/docs#authentication-and-configuration
  # The profile name will be used for `aws sso login`
  # @see: https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-sso.html
  profile = "default"
}

data "aws_availability_zones" "available" {}

locals {
  name   = "electron-eon"
  region = "us-east-1"
  azs    = slice(data.aws_availability_zones.available.names, 0, 2)

  tags = {
    Environment = "production"
    Project     = local.name
    Owner       = "sy"
    CostCenter  = "cc-01"
    CreatedBy   = "terraform"
    CreatedAt   = timestamp()
  }
}
