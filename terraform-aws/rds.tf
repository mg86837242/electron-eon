locals {
  rds_instance_name = "${local.name}-mysql-01"
  db_name           = replace(local.name, "-", "_")
}

########################################################################
# RDS Module
# Referenced example: https://github.com/terraform-aws-modules/terraform-aws-rds/blob/master/examples/complete-mysql/main.tf
# Reference for options: https://registry.terraform.io/modules/terraform-aws-modules/rds/aws/latest
########################################################################

module "rds_default" {
  # Enable either of the two sources:
  # @see: https://developer.hashicorp.com/terraform/language/modules/sources
  # (1) Local Paths: clone the repo `git clone https://github.com/terraform-aws-modules/terraform-aws-rds`,
  # then change the local path reference below
  source = "../../terraform-aws-rds"
  # (2) Terraform Registry: https://registry.terraform.io/modules/terraform-aws-modules/rds/aws/latest,
  # might not work properly if behind a proxy
  # source = "terraform-aws-modules/rds/aws"

  identifier = local.rds_instance_name

  create_db_option_group    = false
  create_db_parameter_group = false

  engine               = "mysql"
  engine_version       = "8.0.35"
  family               = "mysql8.0" # DB parameter group
  major_engine_version = "8.0"      # DB option group
  instance_class       = "db.t4g.micro"

  allocated_storage     = 20
  max_allocated_storage = 100

  db_name  = local.db_name
  username = "dbadmin"
  port     = 3306
  # @see: https://github.com/terraform-aws-modules/terraform-aws-rds?tab=readme-ov-file#input_password
  manage_master_user_password = false
  password                    = var.rds_db_password

  # @see: https://github.com/terraform-aws-modules/terraform-aws-rds/tree/master?tab=readme-ov-file#usage
  create_db_subnet_group = true
  subnet_ids             = var.vpc_private_subnets
  vpc_security_group_ids = [aws_security_group.allow_mysql.id]

  maintenance_window = "Mon:04:30-Mon:05:00"
  backup_window      = "06:15-06:45"

  backup_retention_period = 1
  skip_final_snapshot     = true

  tags = local.tags
}

########################################################################
# Supporting Resources
########################################################################

# Security group best practices: https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group
resource "aws_security_group" "allow_mysql" {
  name_prefix = "${local.name}-mysql"
  description = "Allow inbound access to MySQL from web servers and all outbound traffic"
  vpc_id      = var.vpc_id

  tags = merge(
    local.tags,
    { Name = "${local.name}-mysql" }
  )
}

resource "aws_vpc_security_group_ingress_rule" "allow_mysql_access_from_vpc_ipv4" {
  security_group_id = aws_security_group.allow_mysql.id
  cidr_ipv4         = var.vpc_cidr
  from_port         = 3306
  ip_protocol       = "tcp"
  to_port           = 3306
}

resource "aws_vpc_security_group_egress_rule" "allow_all_outbound_ipv4" {
  security_group_id = aws_security_group.allow_mysql.id
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "-1" # semantically equivalent to all ports
}
