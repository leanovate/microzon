class app_billing::install {
    file { "/opt/billing":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }

    exec { "download billing dist":
        command => "/usr/bin/curl -o /tmp/billing.jar ${app_billing::dist_url}",
    }

    file { "/opt/billing/billing.jar":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///tmp/billing.jar",
        require => Exec["download billing dist"]
    }

    package { "nginx":
        ensure => present
    }    
}