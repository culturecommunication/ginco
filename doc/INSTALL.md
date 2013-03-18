GINCO installation instructions
===============================

Requirements
------------

* The preferred OS/Distribution for GINCO is Linux Centos/RHEL 6.x, but ginco should work on debian based systems.
* Jdk >= 1.7 (work well with OpenJdk) (required)
* PostGreSQL 9.x (tested on PostGreSQL 9.2) (required)
* Tomcat >= 6.x (should works with tomcat 7) (required)
* Apache Maven >= 3.x (required for building)
* Apache Web Server (2.x) (optional)

Installing prerequisite
-----------------------

### JDK ###

> yum install java-1.7.0-openjdk

### PostGreSQL ###

You should first add the PostGreSQL repository
> rpm -Uvh http://yum.postgresql.org/9.2/redhat/rhel-6-x86_64/pgdg-centos92-9.2-6.noarch.rpm

Next install postgresql 
> yum install postgresql postgresql-server postgresql-contrib

### Tomcat ###
 
