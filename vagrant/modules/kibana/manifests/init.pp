class kibana {
    include "kibana::install"
    include "kibana::config"
    include "kibana::service"

    Class["kibana::install"] -> Class["kibana::config"] ~> Class["kibana::service"]
}