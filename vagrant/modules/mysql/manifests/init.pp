
class mysql {
    $root_password = "secret"

    package { "mysql-server":
        ensure => latest
    }

    service { "mysql":
        ensure => running,
        enable => true,
        hasstatus => true,
        hasrestart => true,
        require => Package["mysql-server"]
    }

    mysql_user { 'root@localhost':
      ensure        => present,
      password_hash => mysql_password($root_password),
      require => Service['mysql']
    }

    file { "/root/.my.cnf":
      content => template('mysql/my.cnf.pass.erb'),
      owner   => 'root',
      mode    => '0600',
      require => Mysql_user['root@localhost']
    }
}