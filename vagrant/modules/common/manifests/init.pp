class common {
	File {
        owner => root,
        group => root,
        mode => 644,
        ensure => file,		
	}

    file { "/etc/apt/rkeys":
        owner => root,
        group => root,
        mode => 700,
        ensure => directory,
    }

    file { "/etc/apt/rkeys/GPG-KEY-elasticsearch":
        source => "puppet:///modules/common/GPG-KEY-elasticsearch",
    }

    exec { "apt-key /etc/apt/rkeys/GPG-KEY-elasticsearch":
        command => "/usr/bin/apt-key add /etc/apt/rkeys/GPG-KEY-elasticsearch",
        refreshonly => true,
        subscribe   => File["/etc/apt/rkeys/GPG-KEY-elasticsearch"],
    }

    exec { "apt-key mongo db":
        command => "/usr/bin/apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10"
    }

    file { "/etc/apt/sources.list.d/mongodb.list":
        source => "puppet:///modules/common/mongodb.list",
    }

    file { "/etc/apt/sources.list.d/elasticsearch.list":
        source => "puppet:///modules/common/elasticsearch.list",
    }

    file { "/etc/apt/sources.list.d/logstash.list":
        source => "puppet:///modules/common/logstash.list",
    }

    file { "/etc/apt/sources.list.d/logstashforwarder.list":
        source => "puppet:///modules/common/logstashforwarder.list",
    }
	
    exec { "apt update lists":
        command => "/usr/bin/apt-get update",
        refreshonly => true,
        subscribe   => [
        	File["/etc/apt/sources.list.d/mongodb.list"],
        	File["/etc/apt/sources.list.d/elasticsearch.list"],
        	File["/etc/apt/sources.list.d/logstash.list"],
        	File["/etc/apt/sources.list.d/logstashforwarder.list"],
        ]
    }

    package { "unzip":
        ensure => present
    }
}