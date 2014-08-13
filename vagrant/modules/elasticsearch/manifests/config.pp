class elasticsearch::config {
    file { "/etc/elasticsearch/elasticsearch.yml":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/elasticsearch/elasticsearch.yml"
    }
}