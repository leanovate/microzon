# This is the base image for all other
#
# It should be tagged "dose-base"

FROM ubuntu:14.04

ADD sources/elasticsearch.list /etc/apt/sources.list.d/elasticsearch.list
ADD sources/logstash.list /etc/apt/sources.list.d/logstash.list
ADD sources/logstashforwarder.list /etc/apt/sources.list.d/logstashforwarder.list
ADD sources/mongodb.list /etc/apt/sources.list.d/mongodb.list
ADD sources/GPG-KEY-elasticsearch /etc/apt/rkeys/GPG-KEY-elasticsearch

RUN apt-key add /etc/apt/rkeys/GPG-KEY-elasticsearch
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
RUN apt-get -y update
RUN apt-get install -y --no-install-recommends openjdk-7-jdk
RUN apt-get install -y logstash-forwarder=0.3.1
ADD logstash_forwarder/logstash-forwarder.crt /etc/pki/client/logstash-forwarder.crt
ADD logstash_forwarder/logstash-forwarder /etc/logstash-forwarder