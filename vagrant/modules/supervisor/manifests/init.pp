
class supervisor {
    contain "supervisor::install"
    contain "supervisor::service"
	
    Class["supervisor::install"] ->
    Class["supervisor::service"]
}