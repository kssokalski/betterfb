# Użyj oficjalnego obrazu WildFly
FROM jboss/wildfly:latest

#COPY ./backend/target/betterfb-1.0-SNAPSHOT.war ./backend/src/main/webapp/
# Kopiuj plik WAR do folderu wdrożeniowego
#COPY target/betterfb-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
#COPY ./backend/target/betterfb-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
COPY target/betterfb-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
