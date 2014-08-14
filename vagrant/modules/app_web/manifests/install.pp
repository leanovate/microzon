class app_web::install {
    package { "supervisor":
        ensure => present
    }

    package { "nginx":
        ensure => present
    }    
}