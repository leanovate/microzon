#!/bin/sh

docker push "untoldwind/dose:base-v3"
docker push "untoldwind/dose:consul-v1"
docker push "untoldwind/dose:consul-boot-v1"
docker push "untoldwind/dose:log-v3"
docker push "untoldwind/dose:zipkin-v3"
docker push "untoldwind/dose:mysql-v3" 
docker push "untoldwind/dose:customer-v2"
docker push "untoldwind/dose:product-v2"
docker push "untoldwind/dose:cart-v2"
docker push "untoldwind/dose:billing-v2"
docker push "untoldwind/dose:web-v2"
