GINCO installation instructions
===============================


Requirements
------------

* The preferred OS/Distribution for GINCO is Linux Centos/RHEL 6.x, but it should work on debian based systems.
* Jdk >= 1.7 (work well with OpenJdk) (required)
* PostGreSQL 9.x (tested on PostGreSQL 9.2) (required)
* Tomcat >= 6.x (should works with tomcat 7) (required)
* Apache Maven >= 3.x (required for building)
* SOLR >= 4.2 (required for search)
* Git (required for building)
* Apache Web Server (2.x) (optional)

Installing prerequisite
-----------------------

### JDK ###

> yum install java-1.7.0-openjdk

### PostGreSQL ###

You should first add the PostGreSQL repository
> rpm -Uvh http://yum.postgresql.org/9.2/redhat/rhel-6-x86_64/pgdg-centos92-9.2-6.noarch.rpm

Next install postgresql 
> yum install postgresql92 postgresql92-server postgresql92-contrib

### Tomcat ###

> yum install tomcat6 tomcat6-webapps tomcat6-admin-webapps

### SOLR ###

* Download the bundle Solr

Run the following command:
> wget http://archive.apache.org/dist/lucene/solr/4.2.1/solr-4.2.1.tgz

Extract the following archive:
> tar xvf solr-4.2.1.tgz -C /usr/local/

(The /local/usr can be changed at your convenience ...) 

* Installing Solr server

Go to the directory of Solr:
> cd /usr/local/solr-4.2.1/

Delete the default configuration:
> rm -rf /example/solr/

Rename the example directory:
> mv example ginco

* Installing Solr server

Go in Installing the available configuration Solr of Ginco
Copy available configuration from the Solr to install the Solr server:
> cp -r ginco-solr-conf/solr /usr/local/solr-4.2.1/ginco/ 

* Init script SolR

This script can be modified to be consistent with the installation :

check:

• SOLR_DIR: Location of Solr configuration directory provided

• JAVA_OPTIONS: Subject to change depending on the application setting Solr.

• LOG_FILE: Location of log file

This script must be copied into the directory /etc/init.d/

```
# 
# Solr Control Script 
# 
# chkconfig: 3 80 20 
# Provides: solr 
# Required-Start: $local_fs $remote_fs $network $syslog $named 
# Required-Stop: $local_fs $remote_fs $network $syslog $named 
# Default-Start: 2 3 4 5 
# Default-Stop: 0 1 6 
# Short-Description: Solr and Jetty application Server 
# Description: Starts and stops the solr daemon. 
# 
# To use this script 
# run it as root - it will switch to the specified user 
# It loses all console output - use the log. 
# 
# Starts, stops, and restarts solr 
SOLR_DIR="/usr/local/solr-4.2.1/ginco" 
JAVA_OPTIONS="-Xmx1024m -DSTOP.PORT=8079 -DSTOP.KEY=stopkey -jar start.jar" 
LOG_FILE="/var/log/solr.log" 
case $1 in 
start) 
echo "Starting Solr" 
cd $SOLR_DIR 
java $JAVA_OPTIONS > $LOG_FILE 2>&1 & 
;; 
stop) 
echo "Stopping Solr" 
cd $SOLR_DIR 
java $JAVA_OPTIONS --stop 
;; 
restart) 
$0 stop 
sleep 1 
$0 start 
;; 
*) 
echo "Usage: $0 {start|stop|restart}" >&2 
exit 1 
;; 
esac 
exit 0 
```

* Test operation of Solr

Once launched the service (via the command /etc/init.d/solr start)

Point a browser to the URL http://<url-du-serveur>:8983/solr/

The administration interface of Solr should appear. 


Configuring prerequisites
-------------------------

### Configuring PostGreSQL ###

* Initializing 

> su - postgres -c /usr/pgsql-9.2/bin/initdb

* Configuring connections

Edit the file /var/lib/pgsql/9.2/data/postgresql.conf, and add off uncomment theses lines :

    listen_addresses = 'localhost'
    port = 5432

Next edit the file /var/lib/pgsql/9.2/data/pg_hba.conf for incoming connections, add this line :

    host    all            all              ${IP mask}              md5 

You should configure PostGreSQL for automatic startup on boot
```bash
# /etc/init.d/postgresql-9.2 start 
# chkconfig --levels 235 postgresql-9.2 on
```

* Create the GINCO database

```bash
su - postgres
createdb -E UTF8 ginco
psql
```
```sql
CREATE ROLE ginco WITH SUPERUSER LOGIN PASSWORD '<your pg password here>';
```

### Configuring Tomcat ###

For automatic startup on boot :
> chkconfig --levels 345 tomcat6 on

* Configuring the jdbc connection pool in the file /etc/tomcat6/context.xml

```xml
    <Resource name="jdbc/GincoPool" 
    auth="Container" 
              type="javax.sql.DataSource" 
              driverClassName="org.postgresql.Driver" 
              url="jdbc:postgresql://localhost:5432/ginco?useUnicode=true&amp;characterEncoding=UTF-8&amp;useFastDateParsing=false" 
              username="gingo"                                                                                        
              password="##your pg password here##" 
              maxActive="50" 
              minIdle="10" 
              acquireIncrement="5" 
              removeAbandoned="true" 
              removeAbandonedTimeout="3600" 
              logAbandoned="true" 
     />
```

* Installing the PostGreSQL JDBC driver for tomcat

```bash
# yum install postgresql-jdbc
# cd /usr/share/tomcat6/lib && ln -s /usr/share/java/postgresql92-jdbc.jar
```


Installing from sources
-----------------------

### Installing build prerequisites ###

#### Installing Maven 3 ####

To install maven 3 execute the following commands.

```bash
$ wget ftp://mirrors.ircam.fr/pub/apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.tar.gz
$ tar zxvf apache-maven-3.0.5.tar.gz
# mv apache-maven-3.0.5/ /usr/local/
# nano /etc/profile.d/maven.sh
# export PATH=/usr/local/apache-maven-3.0.x/bin:$PATH
```

#### Installing Git ####

```bash
# yum install git
```

### Configuring maven for auto-deploy on tomcat###

Edit the maven settings.xml (~/.m2/settings.xml) and add a profile :

```xml
<profiles> 
    <profile> 
      <id>ginco-recette</id> 
      <properties> 
         <tomcat.base>http://{server_url}:8080/</tomcat.base> 
	<cargo.tomcat.manager.login>${login_admin_tomcat}</cargo.tomcat.manager.login> 
	<cargo.tomcat.manager.password>${password_admin_tomcat}</cargo.tomcat.manager.password> 
      </properties> 
    </profile> 
  </profiles> 

  <activeProfiles> 
    <activeProfile>ginco-recette</activeProfile>
</activeProfiles>
```

where :
* http://{server_url}:8080/  is the tomcat url where we want to deploy GINCO
* ${login_admin_tomcat} : is the admin login of tomcat (in /etc/tomcat6/tomcat-users.xml)
* ${password_admin_tomcat} : the tomcat admin password


### Getting ginco sources ###

```bash
$ git clone https://github.com/culturecommunication/ginco.git
```

You may want to get a specific tag, then do :

```bash
$ git checkout <tag_name>
```

### Building ginco sources ###

```bash
$ cd ginco
$ mvn clean package
```
the war files should be available in ginco-admin/target

### Deploying GINCO on Tomcat (using cargo) ###

In case of the first installation run :
```bash
$ mvn cargo:deploy
```

In case of a re-installation run :
```bash
$ mvn cargo:redeploye
```

### Deploying GINCO on Tomcat (manually) ###

If cargo don't work you can deploy GINCO manually on tomcat
Run : mvn clean package
Copy the generated ginco-admin/target/ginco-admin.war and ginco-webservices/target/ginco-webservices.war in the webapps tomcat directory.

### Run SQL scripts ###

* Schema and Populate data 

Run : 
```bash
$ psql < ./sql/postgresql92/schema_data.sql

```



### Configuring GINCO ###

To override default Ginco configuration.
Just edit tomcat.conf file, and add option -Dginco-properties=${path_to_custom_properties_files} to JAVA_OPTS
where "${path_to_custom_properties_files}" is the absolute path to your custom ginco.properties file

Default values of ginco.properties
```
#################################################
# Generator configuration
#################################################
#ARK Preferences
application.ark.nma=http://data.culture.fr/thesaurus/resource
application.ark.naan=67717

#################################################
# General configuration
#################################################
#default language
ginco.default.language=fr-FR
solr.url=http://localhost:8983/solr/thesaurus/

#################################################
# Imports configuration
#################################################
#Dublin core namespaces - values should be separated by comma
import.default.top.concept=true
import.skos.default.format=3
import.skos.default.type=3
#This property supports a list of values, separated by comma
import.skos.date.formats=yyyy-MM-dd,yyyy,yyyy-MM-dd HH:mm:ss


#################################################
# LDAP configuration
#################################################
ldap.base.provider.url=ldap://hadoc-int.eqx.intranet:389
ldap.user.base.dn=o=gouv,c=fr
ldap.security.principal=cn=Manager,o=gouv,c=fr
ldap.security.credentials=isfet
ldap.auth.search.filter=(uid={0})

#################################################
# Non LDAP authentication
#################################################
default.user.login=admin
default.user.password=admin
default.user.enabled=disabled

#################################################
# Publish configuration
#################################################
publish.path=/tmp/export/publish/
publish.version.note=Publication
archive.path=/tmp/export/archive/

```

#### Non LDAP Authentication####

You've to configure the following keys in you ginco.properties to configure the basic connection

* default.user.login=admin
* default.user.password=admin
* default.user.enabled=enabled



#### Configuring LDAP ####

If you want to use an LDAP server for authentication.

You've to configure the following keys in you ginco.properties to configure the LDAP connection

* ldap.base.provider.url
* ldap.user.base.dn
* ldap.security.principal
* ldap.security.credentials
* ldap.auth.search.filter


#### Managing Admin Users ####

In the current version GINCO has only a simple right management system.
There's only two profiles.
* Reader : the user can only read the thesaurus
* Admin : the user has access to all ginco functions
 
To configure a user has an admin you have to insert his login name into the  admin_user_id table

```
psql ginco
insert into admin_user_id values ('admin');
```

#### SOLR indexing ###

* Open a web browser and point to : ```http://<server-url>:8080/ginco-admin/services/ui/indexerservice/reindex```

### Running GINCO ###

* Open a web browser and point to : ```http://<server-url>:8080/ginco-admin```
 
Upgrading from V1
------------------

#### Run SQL Scripts ####

If you have a V1 database, there's upgrade scripts to migrate to V2.
Data will be migrated automatically, and nothing will be lost.
You just have to run 

```bash
$ psql < ./sql/postgresql92/upgrade/V2.sql

```
