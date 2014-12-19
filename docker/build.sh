#!/bin/sh

docker build -t "untoldwind/dose:base-v2" base
docker build -t "untoldwind/dose:log-v2" log
docker build -t "untoldwind/dose:zipkin-v1" zipkin
docker build -t "untoldwind/dose:mysql-v3" mysql
docker build -t "untoldwind/dose:customer-v2" customer
docker build -t "untoldwind/dose:product-v2" product
docker build -t "untoldwind/dose:cart-v2" cart
docker build -t "untoldwind/dose:billing-v2" billing
docker build -t "untoldwind/dose:web-v2" web
