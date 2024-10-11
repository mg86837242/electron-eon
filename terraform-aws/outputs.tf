########################################################################
# RDS Outputs
########################################################################

output "rds_instance_identifier" {
  description = "The RDS instance identifier"
  value       = module.rds_default.db_instance_identifier
}

output "rds_subnet_group_id" {
  description = "The db subnet group name"
  value       = module.rds_default.db_subnet_group_id
}
