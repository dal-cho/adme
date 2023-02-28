# APP
FROM azul/zulu-openjdk:11.0.16.1
WORKDIR /app

# builder 이미지에서 jar 파일만 복사
COPY ./*-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]
