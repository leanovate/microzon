
class app_mysql {
	contain "app_mysql::cart"
	contain "app_mysql::customer"
	contain "app_mysql::billing"
	contain "app_mysql::config"
	contain "app_mysql::service"
}