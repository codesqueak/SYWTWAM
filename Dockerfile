FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
EXPOSE 8081
ADD SYWTWAM-0.2.1-SNAPSHOT-BOOT.jar ./app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
# -Dspring.cloud.consul.enabled=false -Dspring.cloud.bus.enabled=false
ENTRYPOINT ["java","-Dspring.cloud.consul.enabled=false","-Dspring.cloud.bus.enabled=false","-Dspring.profiles.active=aws","-jar","./app.jar"]