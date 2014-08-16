class logstash::install {

    package { "logstash":
        ensure => "1.4.2-1-2c0f5a1",
    }
}