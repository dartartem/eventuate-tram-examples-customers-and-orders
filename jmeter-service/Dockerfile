FROM openjdk:14-jdk-alpine3.10
RUN wget -q -O - http://mirror.linux-ia64.org/apache//jmeter/binaries/apache-jmeter-5.2.1.tgz |  tar -xzf - -C /usr/local
RUN mkdir /usr/local/plans
RUN mkdir /usr/local/results
CMD java ${JAVA_OPTS} -jar jmeter-service-*.jar
COPY build/libs/jmeter-service-*.jar .
COPY TestPlan.jmx /usr/local/plans
