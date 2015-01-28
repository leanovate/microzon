
class consul_agent {
	contain "consul_agent::install"
    contain "consul_agent::config"
    contain "consul::service"

    Class["consul_agent::install"] ->
    Class["consul_agent::config"]
}