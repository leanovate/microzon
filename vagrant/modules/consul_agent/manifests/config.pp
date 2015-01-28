
class consul_agent::config {
	file { "/etc/supervisor/conf.d/consul.conf":
        source => "puppet:///modules/consul/consul.conf",
        notify => Service["supervisor"]
    }

    file { "/etc/consul.d/config.json":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/consul/config.json"
    }
}