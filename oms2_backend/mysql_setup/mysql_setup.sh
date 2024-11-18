#!/bin/bash 

# 변수 이름 
SOURCE_CONTAINER="source"
REPLICA_CONTAINER="replica"
CREATE_DB_COMMAND="CREATE DATABASE oms2 CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

# 첫 번째 인자 확인 
ACTION=$1

if [ "$ACTION" == "down" ]; then
    echo "mysql 종료..."
    docker stop $REPLICA_CONTAINER 
    docker rm $REPLICA_CONTAINER

    docker stop $SOURCE_CONTAINER
    docker rm $SOURCE_CONTAINER
    exit 0
elif [ "$ACTION" == "up" ]; then
    echo "mysql 시작..."
else 
    echo "Usage: $0 {up|down}"
    exit 1
fi


# 마운트 하기 위한 디렉토리 생성
echo "Creating directories for MySQL data..."
if [ ! -d "./source_data" ]; then
    rm -rf ./source_data
fi
if [ ! -d "./replica_data" ]; then
    rm -rf ./replica_data
fi

mkdir -p ./source_data
mkdir -p ./replica_data

# .env 파일 경로
ENV_FILE_PATH="../.env"

# .env 파일 로드
if [ -f $ENV_FILE_PATH ]; then
    export $(cat $ENV_FILE_PATH | xargs) 
else 
    echo "Error : .env file not found at $ENV_FILE_PATH"
    exit 1
fi 

# docker-compose 실행, source, replica MySQL 컨테이너 생성 
echo "Creating Source, Replica Docker Container..."
docker-compose --env-file $ENV_FILE_PATH up -d

# db 생성
echo "Creating database oms2 in the Source container..."
docker exec -i $SOURCE_CONTAINER mysql -uroot -p"$MYSQL_ROOT_PASSWORD" -e "$CREATE_DB_COMMAND"

echo "Creating database oms2 in the Replica container..."
docker exec -i $REPLICA_CONTAINER mysql -uroot -p"$MYSQL_ROOT_PASSWORD" -e "$CREATE_DB_COMMAND"

# source에 replica 사용자 생성 
docker exec -i source mysql -uroot -p$MYSQL_ROOT_PASSWORD -e "
CREATE USER 'replica'@'%' IDENTIFIED BY '$MYSQL_REPLICA_PASSWORD';
GRANT REPLICATION SLAVE ON *.* TO 'replica'@'%';
FLUSH PRIVILEGES;"

# 바로 가져오면 MASTER_STATUS 정보를 못 가져온다. 시간을 두어 가져오게 한다. 
sleep 3 

# replica에 Master 설정을 위한 로그 파일과 위치 가져오기  
MASTER_STATUS=$(docker exec -i $SOURCE_CONTAINER mysql -uroot -p$MYSQL_ROOT_PASSWORD -e "SHOW MASTER STATUS\G")
MASTER_LOG_FILE=$(echo "$MASTER_STATUS" | grep File | awk '{print $2}')
MASTER_LOG_POS=$(echo "$MASTER_STATUS" | grep Position | awk '{print $2}')

if [ -z "$MASTER_LOG_FILE" ] || [ -z "$MASTER_LOG_POS" ]; then
    echo "Error: Could not retrieve MASTER_LOG_FILE or MASTER_LOG_POS from Source."
    exit 1
fi

echo "MASTER_LOG_FILE: $MASTER_LOG_FILE"
echo "MASTER_LOG_POS: $MASTER_LOG_POS"

# slave IO 스레드 중지 
docker exec -i replica mysql -uroot -p"$MYSQL_ROOT_PASSWORD" -e "STOP SLAVE IO_THREAD FOR CHANNEL '';"

# Replica 설정
echo "Configuring Replica container..."
CHANGE_MASTER_COMMAND="
    CHANGE MASTER TO MASTER_HOST='source',
    MASTER_USER='replica', 
    MASTER_PASSWORD='$MYSQL_REPLICA_PASSWORD', 
    MASTER_LOG_FILE='$MASTER_LOG_FILE', 
    MASTER_LOG_POS=$MASTER_LOG_POS;"
docker exec -i $REPLICA_CONTAINER mysql -uroot -p$MYSQL_ROOT_PASSWORD -e "$CHANGE_MASTER_COMMAND"

sleep 3 

# Slave 시작
echo "Starting replication on Replica container..."
docker exec -i $REPLICA_CONTAINER mysql -uroot -p$MYSQL_ROOT_PASSWORD -e "START SLAVE;"
