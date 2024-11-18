# 1. 베이스 이미지 선택 (여기서는 OpenJDK 17을 사용)
FROM openjdk:17-jdk-slim

# 2. JAR 파일을 컨테이너에 복사
ARG JAR_FILE=build/libs/oms2-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} oms2.jar

# 3. 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/oms2.jar"]
