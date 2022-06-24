#!/bin/bash
export "JAVA_HOME=/etc/alternatives/jre"
export "GINCO_CLI_DIR=/home/satis/ginco/ginco-cli"
export "JAVA_OPTIONS=-Dfile.encoding=UTF-8 -jar ginco-cli-2.0.9.10.jar"

cd $GINCO_CLI_DIR
$JAVA_HOME/bin/java $JAVA_OPTIONS -i $1
