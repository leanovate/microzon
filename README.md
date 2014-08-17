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
  * Mysql backend
  

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
#!bash
cd <project home>/customer
./gradlew clean dist
```
* web frontend service
```
#!bash
cd <project home>/web
./activator clean dist
```

### Start the cluster

```
#!bash
cd <project home>/vagrant
vagrant up
```

### Enjoy the show

* The shop demo itselft: http://192.168.254.11
* The logging backend: http://192.168.254.10