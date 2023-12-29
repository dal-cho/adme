# APP
FROM openjdk:11-slim
WORKDIR /app

# builder 이미지에서 jar 파일만 복사
COPY ./*-SNAPSHOT.jar ./app.jar

RUN apt-get update
RUN apt-get install ffmpeg

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
