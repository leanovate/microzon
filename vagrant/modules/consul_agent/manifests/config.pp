
class consul_agent::config {
	file { "/etc/supervisor/conf.d/consul.conf":
        source => "puppet:///modules/consul_agent/consul.conf",
        notify => Service["supervisor"],
        require => Package["supervisor"]
    }

	file { "/etc/supervisor/conf.d/consul-template.conf":
        source => "puppet:///modules/consul_agent/consul-template.conf",
        notify => Service["supervisor"],
        require => Package["supervisor"]
    }

    file { "/etc/consul.d/config.json":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/consul_agent/config.json"
    }

    file { "/etc/consul-templates/templates.conf":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/consul_agent/templates.conf"
    }

    file { "/etc/consul-templates/logstash-forwarder.ctmpl":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/consul_agent/logstash-forwarder.ctmpl"
    }
}