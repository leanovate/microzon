Micro service shop example
==========================

# Getting started

## Requirements

* A machine with 8 Gb ram at the very least (16 Gb is much better)
* Java JDK 1.7
* Virtual Box 4.3
* Vagrant 1.6
* An internet connection

## Getting started

### Building the micro service distributions

All build scripts will create an distribution archive in <project home>/vagrant/dist

* customer service
{{{
cd <project home>/customer
./gradlew clean dist
}}}
* web frontend service
{{{
cd <project home>/web
./activator clean dist
}}}

### Start the cluster

{{{
cd <project home>/vagrant
vagrant up
}}}

### Enjoy the show

* The shop demo itselft: http://192.168.254.11
* The logging backend: http://192.168.254.10
