#using latest payara image
FROM payara/server-full:latest

#changing user to root to install mysql connector
USER root
#mysql lib
COPY mysql-connector-j-8.0.33.jar /opt/payara/appserver/glassfish/lib
#COPY target/backend.war $DEPLOY_DIR
#placing war file with all the code
COPY target/backend.war /opt/payara/appserver/glassfish/domains/domain1/autodeploy/
#payara config file
COPY domain.xml $PAYARA_DIR/glassfish/domains/domain1/config
#script added to assure that mysql is up and running first
#COPY wait-for-it.sh /wait-for-it.sh
#installing mysql for testing purposes
RUN apt-get update && apt-get install -y mysql-client

#making the script executable
#RUN chmod +x /wait-for-it.sh
#executing the script
#CMD /wait-for-it.sh mysql:3306 --timeout=100 -- /opt/payara/appserver/bin/asadmin start-domain --verbose

#exposing the port
EXPOSE 8080
