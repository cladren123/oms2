# oms2

프로젝트 기술 
* Spring Boot 3.3.5
* MySQL 8.0.30
* Docker 27.3.1
* Nginx 1.27.2
* Ubuntu 24:04

### DB 이중화 설정 

DB 이중화 구성 <br>
3307 : Source(Master) Docker Container <br>
3308 : Replica(Slave) Docker Container <br>


실행 방법 <br>
/oms2_backend/mysql_setup 디렉토리에 mysql_setup.sh 스크립트 실행 

```
# 실행 
./mysql_setup.sh up

# 종료
./mysql_setup.sh down
```

<br>

### Spring Boot, Nginx 설정 

Spring Boot 2개 서버
* blue : 8081, 8082
* green : 8083, 8084 

Nginx 
* 로드밸런싱 : 라운드 로빈 
* 장애 발생 시 정상 서버로 트래픽 옮김 

실행 방법 
spring boot 빌드
```
./gradlew clean build
```

Spring Boot 이미지 생성
```
docker build -t oms2 . 
```

배포 스크립트 실행 
/oms2_backend 디렉토리에 blue_green_deploy.sh 실행
```
./blue_green_deploy.sh 
``` 

처음 시작할 경우 oms2-blue1, oms2-blue2, oms2-nginx 컨테이너 생성 <br>
두 번재 시작할 경우 : oms2-green1, oms2-green2 생성되고 oms2-nginx 설정 변경, oms2-blue1, oms2-blue2 내려감 <br>
이후 blue, green 반복 및 nginx 설정 변경 

