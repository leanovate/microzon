
class app_mongo {
	contain "app_mongo::install"
	contain "app_mongo::config"
	contain "app_mongo::service"

	Class["app_mongo::install"] ->
	Class["app_mongo::config"] ~>
	Class["app_mongo::service"]
}