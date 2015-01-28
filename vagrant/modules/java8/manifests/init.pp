class java8 {
	apt::ppa { 'webupd8team/java':
	}

	
    package { "openjdk-7-jdk":
        ensure => present
    }
}