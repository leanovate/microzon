class java8 {
    exec { 'apt_update':
        command     => "/usr/bin/apt-get update",
        user        => 'root',
        logoutput   => 'on_failure',
    	refreshonly => true,
        try_sleep   => 1
    }

    exec { "set java 8 selections":
    	command     => "/bin/echo 'oracle-java8-installer shared/accepted-oracle-license-v1-1 select true' | /usr/bin/debconf-set-selections",
        user        => 'root',
        logoutput   => 'on_failure',
    	refreshonly => true,
    }

    exec { "add-apt-repository-webupd8team":
        command     => "/usr/bin/add-apt-repository ppa:webupd8team/java",
        created     => "/etc/apt/sources.list.d/webupd8team-java-trusty.list",
        user        => 'root',
        logoutput   => 'on_failure',
        notify      => [Exec['apt_update'], Exec['set java 8 selections']],
    }
}