# mysql setup

해당 프로젝트 DB 이중화 Source Replica 구조를 이루고 있습니다. (Master - Slave 방식)

mysql_setup.sh 스크립트를 실행하여 Source, Replica 도커 컨테이너를 다룹니다. 


## MySQL 구성 

스크립트 실행 권한 추가 
```
chmod +x mysql_setup.sh 
```

스크립트 실행 
```
./mysql_setup.sh
```

사용자 목록 확인
```
select user, host from mysql.user;
```

