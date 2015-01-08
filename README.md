Micro service shop example
==========================

# Some words of warning

This is a demonstration how microservices may interact with each other and how to debug them. This is not supposed to be or become a real shop implementation. Most importantly: The current implementation intentionally contains several design flaws as they might occure in real life product development.

# Used technologies

For simplicity all services are supposed to run on a Java-VM.

* Web frontend
  * Based on Play 2.3 / Scala 2.11
  * Does not have any kind of persistence
* Customer service
  * Based on Spring boot
  * Mysql backend via JPA/Hibernate
* Product backend
  * Based on Spray 1.3.1
  * MongoDB backend via ReactiveMongo
* (Shopping)Cart backend
  * Based on Scalatra with embedded Jetty
  * Mysql backend via ScalalikeJdbc

# Getting started

## Requirements

* When using the vagrant environment we start up to 7 VMs with 8 Gb memory total, therefore a machine with at least 16 Gb is advisable
* Java JDK 1.7 (OpenJDK and OracleJDK are both working)
* An internet connection (expect lots of stuff being downloaded)
* Either
  * Virtual Box 4.3
  * Vagrant 1.6
* ... or
  * Docker 1.4

The setup has been tested with MocOS (Maverick/Yosemite) and Ubuntu 14.04. If you are using a different system you might encounter so far unknown problems.

## Getting started

Since this is a project for experimentation it is possible to run the stuff via docker or vagrant/virtualbox.

### Run via docker

Just move to the "docker" subdirectory and execute the run.sh script
```
cd docker
sh ./run.sh
```
(all the images should be available via the public docker hub)

After this the following URLs should work:
* http://localhost:8080 -> The shop itself
* http://localhost:8000 -> ELK (Elasicsearch/Logstash/Kibana)
* http://localhost:8001 -> Zipkin
* http://localhost:8002 -> Consul web ui
 
To build the images from scratch you fist have to build distributions of all the microservices (see below) and run the "build.sh" script.

**For Mac users**: You have should use boot2docker with can be either downloaded here http://boot2docker.io/ or installed via homebrew
```
brew install boot2docker
```
Ensure that the boot2docker VM has enough RAM, i.e. to run everything the VM should have at least 6GB
```
boot2docker init -m 6144
boot2docker up
```
Also note that you have to use the IP returned by `boot2docker ip` instead of localhost.

### Building the micro service distributions

All build scripts will create an distribution archive in <project home>/vagrant/dist

Note: As in real life several different build tools/configs are used for each project. Pay close attention to each of the commands, it is not always the same.

* customer service
```
cd <project home>/customer
./gradlew clean dist
```
* product service
```
cd <project home>/customer
./activator clean assembly
```
* web frontend service
```
cd <project home>/web
./activator clean dist
```

### Start the cluster via vagrant

```
#!bash
cd <project home>/vagrant
vagrant up
```

### Populating the databases

* product database
For docker:
```
python product/src/main/scripts/populate.py localhost:8090
```
resp. for Mac users:
```
python product/src/main/scripts/populate.py $(boot2docker ip):8090
```

### Enjoy the show

* The shop demo itselft: http://192.168.254.11
* The logging backend: http://192.168.254.10
