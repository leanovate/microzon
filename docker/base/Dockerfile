# This is the base image for all other
#
# It should be tagged "untoldwind/dose-base". 
# This is also available via the public docker hub.

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
RUN apt-get install -y openssh-server
RUN apt-get install -y supervisor
ADD logstash_forwarder/logstash-forwarder.crt /etc/pki/client/logstash-forwarder.crt
ADD logstash_forwarder/logstash-forwarder /etc/logstash-forwarder
ADD sources/sshd.conf /etc/supervisor/conf.d/sshd.conf
RUN mkdir /var/run/sshd
RUN echo 'root:root' | chpasswd
RUN sed -i 's/PermitRootLogin without-password/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd

ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile
