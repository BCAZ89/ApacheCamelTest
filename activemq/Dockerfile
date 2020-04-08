FROM openjdk:8-jre-alpine
RUN wget -O activemq.tar.gz http://archive.apache.org/dist/activemq/5.15.12/apache-activemq-5.15.12-bin.tar.gz
RUN tar -xzf activemq.tar.gz
#Expose ports (console and broker) and run activemq
#EXPOSE 8161 61616
CMD ["apache-activemq-5.15.12/bin/activemq", "start"]