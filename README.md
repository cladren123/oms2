# oms2

프로젝트 기술 
* Spring Boot
* MySQL
* Docker 
* Nginx 
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
