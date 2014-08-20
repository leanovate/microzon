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

* A machine with 8 Gb ram at the very least (16 Gb is much better)
* Java JDK 1.7
* Virtual Box 4.3
* Vagrant 1.6
* An internet connection

## Getting started

### Building the micro service distributions

All build scripts will create an distribution archive in <project home>/vagrant/dist

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

### Start the cluster

```
#!bash
cd <project home>/vagrant
vagrant up
```

### Populating the databases

* product database
```
python product/src/main/scripts/populate.py
```

### Enjoy the show

* The shop demo itselft: http://192.168.254.11
* The logging backend: http://192.168.254.10