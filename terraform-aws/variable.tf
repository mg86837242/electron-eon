variable "vpc_id" {
  type        = string
  sensitive   = true
  description = "The ID of the VPC created by AWS Copilot CLI"
}

variable "vpc_private_subnets" {
  type        = list(string)
  sensitive   = true
  description = "List of IDs of private subnets inside the VPC that is created by AWS Copilot CLI"
}

variable "vpc_cidr" {
  type        = string
  default     = "10.0.0.0/16"
  description = "The IPv4 CIDR block for the VPC"
}

variable "rds_db_password" {
  type        = string
  sensitive   = true
  description = "[The password for the master DB user of the RDS instance](https://github.com/terraform-aws-modules/terraform-aws-rds?tab=readme-ov-file#input_password)"
}
