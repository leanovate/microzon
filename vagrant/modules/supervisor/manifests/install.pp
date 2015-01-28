
class supervisor::install {
    package { "supervisor":
        ensure => present
    }	
}