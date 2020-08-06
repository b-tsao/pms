#!/bin/sh
export CLASSPATH=".:dist/odinms.jar:dist/mina-core.jar:dist/slf4j-api.jar:dist/slf4j-jdk14.jar:dist/mysql-connector-java-bin.jar"
java -Dnet.sf.odinms.recvops=recvops.properties \
-Dnet.sf.odinms.sendops=sendops.properties \
-Dnet.sf.odinms.wzpath=. \
-Djavax.net.ssl.keyStore=world.keystore \
-Djavax.net.ssl.keyStorePassword=worldkeystorepassword \
-Djavax.net.ssl.trustStore=world.truststore \
-Djavax.net.ssl.trustStorePassword=worldtruststorepassword \
net.sf.odinms.net.world.WorldServer
