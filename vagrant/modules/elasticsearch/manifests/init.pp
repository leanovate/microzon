
class elasticsearch {
    include "elasticsearch::install"
    include "elasticsearch::config"
    include "elasticsearch::service"

    Class["elasticsearch::install"] -> Class["elasticsearch::config"] ~> Class["elasticsearch::service"]
}