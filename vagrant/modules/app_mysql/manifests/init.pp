
class app_mysql {
	contain "app_mysql::cart"
	contain "app_mysql::customer"
	contain "app_mysql::billing"
	contain "app_mysql::config"
	contain "app_mysql::service"

	Class["app_mysql::config"] ~>
	Class["app_mysql::service"]

	Class["app_mysql::service"] -> Class["app_mysql::cart"]
	Class["app_mysql::service"] -> Class["app_mysql::customer"]
	Class["app_mysql::service"] -> Class["app_mysql::billing"]
}