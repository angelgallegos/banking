FROM gradle:jdk21-alpine
RUN mkdir /home/gradle/buildWorkspace
COPY . /home/gradle/buildWorkspace

WORKDIR /home/gradle/buildWorkspace
RUN ls /home/gradle/buildWorkspace
RUN gradle build --no-daemon
RUN ls /home/gradle/buildWorkspace/build/libs/
RUN mv build/libs/*.jar build/libs/app.jar
RUN cp /home/gradle/buildWorkspace/build/libs/app.jar /home/gradle/buildWorkspace/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]