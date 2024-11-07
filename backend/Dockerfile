FROM payara/server-full:latest

USER root
COPY mysql-connector-j-8.0.33.jar /opt/payara/appserver/glassfish/lib
#COPY target/backend.war $DEPLOY_DIR
COPY target/backend.war /opt/payara/appserver/glassfish/domains/domain1/autodeploy/
COPY domain.xml $PAYARA_DIR/glassfish/domains/domain1/config
COPY wait-for-it.sh /wait-for-it.sh
RUN apt-get update && apt-get install -y mysql-client
RUN chmod +x /wait-for-it.sh


CMD /wait-for-it.sh mysql:3306 --timeout=100 -- /opt/payara/appserver/bin/asadmin start-domain --verbose


EXPOSE 8080
