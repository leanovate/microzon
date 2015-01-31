
class app_mongo::config {
    File {
        owner => root,
        group => root,
        mode => 644,
        ensure => file        
    }

    file { "/etc/mongod.conf":
        source => "puppet:///modules/app_mongo/mongod.conf",
    }

    file { "/etc/consul.d/mongo.json":
        source => "puppet:///modules/app_mongo/mongo.json",
        notify => Service["supervisor"]
    }
}