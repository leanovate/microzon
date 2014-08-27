# This is the base image for all other
#
# It should be tagged "ubuntu-puppet-base"

FROM ubuntu:14.04

RUN apt-get -y update
RUN apt-get -y install ruby
RUN echo "gem: --no-ri --no-rdoc" > ~/.gemrc
RUN gem install puppet
